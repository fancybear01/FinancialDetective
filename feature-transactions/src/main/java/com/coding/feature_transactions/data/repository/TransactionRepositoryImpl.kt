package com.coding.feature_transactions.data.repository

import com.coding.core.data.local.dao.AccountDao
import com.coding.core.data.local.dao.CategoryDao
import com.coding.core.data.local.entity.TransactionEntity
import com.coding.core.data.remote.dto.transactions_dto.TransactionRequestDto
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
import com.coding.feature_transactions.data.local.source.TransactionLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

class TransactionRepositoryImpl @Inject constructor(
    private val remoteDataSource: TransactionRemoteDataSource,
    private val localDataSource: TransactionLocalDataSource,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val connectivityObserver: ConnectivityObserver
) : TransactionRepository {

    override fun getTransactionsStream(
        accountId: String, startDate: String, endDate: String
    ): Flow<List<Transaction>> {
        return localDataSource.getTransactionsStream(accountId, startDate, endDate).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getTransactionById(id: Int): Result<Transaction, NetworkError> {
        return remoteDataSource.getTransactionById(id).map { it.toDomain() }
    }

    override suspend fun createTransaction(
        accountId: Int, categoryId: Int, transactionDate: ZonedDateTime,
        amount: Double, comment: String
    ): Result<Unit, NetworkError> {
        val newTransaction = TransactionEntity(
            remoteId = null, accountId = accountId, categoryId = categoryId, amount = amount,
            transactionDate = transactionDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            comment = comment,
            createdAt = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            updatedAt = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            isSynced = false, isDeleted = false, clientLastUpdatedAt = System.currentTimeMillis()
        )
        localDataSource.insertOrUpdate(newTransaction)
        repositoryScope.launch { syncOfflineCreations() }
        return Result.Success(Unit)
    }

    override suspend fun updateTransaction(
        id: Int, accountId: Int, categoryId: Int, transactionDate: ZonedDateTime,
        amount: Double, comment: String
    ): Result<Unit, NetworkError> {
        localDataSource.markAsUpdated(
            remoteId = id, accountId = accountId, categoryId = categoryId,
            transactionDate = transactionDate, amount = amount, comment = comment,
            timestamp = System.currentTimeMillis()
        )
        repositoryScope.launch { syncOfflineUpdates() }
        return Result.Success(Unit)
    }

    override suspend fun deleteTransaction(id: Int): Result<Unit, NetworkError> {
        localDataSource.markAsDeleted(id, System.currentTimeMillis())
        repositoryScope.launch { syncOfflineDeletions() }
        return Result.Success(Unit)
    }

    override suspend fun syncTransactionsForPeriod(
        accountId: String, startDate: String, endDate: String
    ): Result<Unit, NetworkError> {
        return remoteDataSource.getTransactionsForPeriod(accountId, startDate, endDate).onSuccess { dtoList ->
            val entities = dtoList.map { it.toEntity(isSynced = true) }
            localDataSource.upsertTransactions(entities)
        }.map {}
    }

    override suspend fun syncOfflineCreations() {
        localDataSource.getUnsyncedTransactions().filter { it.remoteId == null && !it.isDeleted }
            .forEach { localTransaction ->
                val request = localTransaction.toRequestDto()
                remoteDataSource.createTransaction(request).onSuccess { responseDto ->
                    categoryDao.upsertAll(listOf(responseDto.category.toEntity()))
                    localDataSource.deleteLocalTransaction(localTransaction.localId)
                    localDataSource.insertOrUpdate(responseDto.toEntity(isSynced = true))
                }
            }
    }

    override suspend fun syncOfflineUpdates() {
        localDataSource.getUnsyncedTransactions().filter { it.remoteId != null && !it.isDeleted }
            .forEach { localTransaction ->
                val remoteId = localTransaction.remoteId
                if (remoteId != null) {
                    val request = localTransaction.toRequestDto()
                    remoteDataSource.updateTransaction(remoteId, request).onSuccess { responseDto ->
                        categoryDao.upsertAll(listOf(responseDto.category.toEntity()))
                        localDataSource.insertOrUpdate(responseDto.toEntity(isSynced = true))
                    }
                }
            }
    }

    override suspend fun syncOfflineDeletions() {
        val toDelete = localDataSource.getUnsyncedTransactions().filter { it.isDeleted }
        toDelete.forEach { transactionToDelete ->
            transactionToDelete.remoteId?.let { remoteId ->
                remoteDataSource.deleteTransaction(remoteId).onSuccess {
                    localDataSource.deleteSyncedTransactions(listOf(remoteId))
                }
            }
        }
    }
}