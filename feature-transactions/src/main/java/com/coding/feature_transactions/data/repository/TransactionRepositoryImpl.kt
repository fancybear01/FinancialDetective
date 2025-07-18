package com.coding.feature_transactions.data.repository

import androidx.lifecycle.asFlow
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.coding.core.data.local.entity.TransactionEntity
import com.coding.core.data.util.NetworkError
import com.coding.core.data.util.Result
import com.coding.core.data.util.map
import com.coding.core.data.mapper.toDomain
import com.coding.core.data.mapper.toEntity
import com.coding.core.data.mapper.toRequestDto
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.core.data.util.onSuccess
import com.coding.feature_transactions.data.remote.source.TransactionRemoteDataSource
import com.coding.core.domain.model.transactions_models.Transaction
import com.coding.core.domain.repository.TransactionRepository
import com.coding.core.util.SyncWorker
import com.coding.feature_transactions.data.local.source.TransactionLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val remoteDataSource: TransactionRemoteDataSource,
    private val localDataSource: TransactionLocalDataSource,
    private val connectivityObserver: ConnectivityObserver,
    private val workManager: WorkManager
) : TransactionRepository {

    override fun getTransactionsStream(
        accountId: String, startDate: String, endDate: String
    ): Flow<List<Transaction>> {
        return localDataSource.getTransactionsStream(accountId, startDate, endDate)
            .map { list -> list.mapNotNull { it.toDomain() } }
    }

    override suspend fun getTransactionById(id: Int): Result<Transaction, NetworkError> {
        return remoteDataSource.getTransactionById(id).map { it.toDomain() }
    }

    override suspend fun createTransaction(
        accountId: Int, categoryId: Int, transactionDate: ZonedDateTime,
        amount: Double, comment: String
    ): Result<Unit, NetworkError> {
        val newTransaction = TransactionEntity(
            id = null,
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            comment = comment,
            createdAt = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            updatedAt = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            isSynced = false,
            isDeleted = false
        )
        localDataSource.upsert(newTransaction)

        enqueueSyncWork()

        return Result.Success(Unit)
    }

    override suspend fun updateTransaction(
        id: String, accountId: Int, categoryId: Int, transactionDate: ZonedDateTime,
        amount: Double, comment: String
    ): Result<Unit, NetworkError> {

        val localTransaction = if (id.startsWith("local_")) {
            val localId = id.removePrefix("local_").toLongOrNull() ?: return Result.Error(NetworkError.BAD_REQUEST)
            localDataSource.getByLocalId(localId)?.transaction
        } else {
            val remoteId = id.toIntOrNull() ?: return Result.Error(NetworkError.BAD_REQUEST)
            localDataSource.getByRemoteId(remoteId)
        }

        if (localTransaction == null) {
            return Result.Error(NetworkError.NOT_FOUND)
        }

        val updatedTransaction = localTransaction.copy(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            comment = comment,
            updatedAt = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            isSynced = false
        )
        localDataSource.upsert(updatedTransaction)
        enqueueSyncWork()
        return Result.Success(Unit)
    }

    override suspend fun deleteTransaction(id: String): Result<Unit, NetworkError> {
        if (id.startsWith("local_")) {
            val localId = id.removePrefix("local_").toLongOrNull()
                ?: return Result.Error(NetworkError.BAD_REQUEST)

            localDataSource.deleteByLocalId(localId)

        } else {
            val remoteId = id.toIntOrNull()
                ?: return Result.Error(NetworkError.BAD_REQUEST)

            localDataSource.markAsDeleted(remoteId)
            enqueueSyncWork()
        }

        return Result.Success(Unit)
    }

    override suspend fun syncTransactionsForPeriod(
        accountId: String, startDate: String, endDate: String
    ): Result<Unit, NetworkError> {
        if (!connectivityObserver.isConnected.first()) return Result.Success(Unit)

        return remoteDataSource.getTransactionsForPeriod(accountId, startDate, endDate)
            .onSuccess { dtoList ->
                if (dtoList.isEmpty()) return@onSuccess
                val locallyDeletedIds = localDataSource.getUnsyncedDeletions().mapNotNull { it.id }.toSet()
                val serverEntities = dtoList
                    .filter { it.id !in locallyDeletedIds }
                    .map { it.toEntity(isSynced = true) }

                if (serverEntities.isEmpty()) return@onSuccess

                val remoteIds = serverEntities.mapNotNull { it.id }

                localDataSource.deleteByRemoteIds(remoteIds)
                localDataSource.upsertAll(serverEntities)

            }.map {}
    }

    override suspend fun syncOfflineCreationsAndUpdates() {
        if (!connectivityObserver.isConnected.first()) return

        localDataSource.getUnsyncedCreationsOrUpdates().forEach { localTransaction ->
            val request = localTransaction.toRequestDto()

            val remoteId = localTransaction.id
            val result = if (remoteId != null) {
                remoteDataSource.updateTransaction(remoteId, request)
            } else {
                remoteDataSource.createTransaction(request)
            }

            result.onSuccess { responseDto ->
                responseDto.id?.let { newRemoteId ->
                    localDataSource.setSynced(localTransaction.localId, newRemoteId)
                }
            }
        }
    }

    override suspend fun syncOfflineDeletions() {
        if (!connectivityObserver.isConnected.first()) return

        localDataSource.getUnsyncedDeletions().forEach { transactionToDelete ->
            transactionToDelete.id?.let { remoteId ->
                remoteDataSource.deleteTransaction(remoteId).onSuccess {
                    localDataSource.deletePermanently(remoteId)
                }
            }
        }
    }

    override suspend fun syncAllPending() {
        if (!connectivityObserver.isConnected.first()) return

        syncOfflineCreationsAndUpdates()
        syncOfflineDeletions()
    }

    override suspend fun syncAllData(accountId: String): Result<Unit, NetworkError> {
        if (!connectivityObserver.isConnected.first()) return Result.Success(Unit)

        localDataSource.getUnsyncedCreationsOrUpdates().forEach { localTransaction ->
            val request = localTransaction.toRequestDto()

            val remoteId = localTransaction.id

            val result = if (remoteId != null) {
                remoteDataSource.updateTransaction(remoteId, request)
            } else {
                remoteDataSource.createTransaction(request)
            }

            result.onSuccess { responseDto ->
                responseDto.id?.let { newRemoteId ->
                    localDataSource.setSynced(localTransaction.localId, newRemoteId)
                }
            }
        }

        return remoteDataSource.getAllTransactionsForAccount(accountId)
            .onSuccess { dtoList ->
                localDataSource.deleteAllForAccount(accountId)
                localDataSource.upsertAll(dtoList.map { it.toEntity(isSynced = true) })
            }.map {}
    }

    override fun getSyncWorkInfo(): Flow<WorkInfo?> {
        return workManager
            .getWorkInfosForUniqueWorkLiveData("sync_pending_transactions")
            .asFlow()
            .map { workInfoList ->
                workInfoList.firstOrNull()
            }
    }

    override suspend fun getLocalTransactionById(localId: Long): Result<Transaction, NetworkError> {
        val transactionWithDetails = localDataSource.getByLocalId(localId)
        return if (transactionWithDetails != null) {
            val transaction = transactionWithDetails.toDomain()
            if (transaction != null) {
                Result.Success(transaction)
            } else {
                Result.Error(NetworkError.NOT_FOUND)
            }
        } else {
            Result.Error(NetworkError.NOT_FOUND)
        }
    }

    private fun enqueueSyncWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(
            "sync_pending_transactions",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }
}