package com.coding.feature_transactions.data.local.source

import com.coding.core.data.local.dao.TransactionDao
import com.coding.core.data.local.entity.TransactionEntity
import com.coding.core.data.local.relation.TransactionWithDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionLocalDataSource @Inject constructor(private val transactionDao: TransactionDao) {

    fun getTransactionsStream(accountId: String, startDate: String, endDate: String): Flow<List<TransactionWithDetails>> {
        return transactionDao.getTransactionsForPeriod(accountId, startDate, endDate)
    }

    suspend fun upsertAll(transactions: List<TransactionEntity>) {
        transactionDao.upsertAll(transactions)
    }

    suspend fun upsert(transaction: TransactionEntity) {
        transactionDao.upsert(transaction)
    }

    suspend fun setSynced(localId: Long, remoteId: Int) {
        transactionDao.setSynced(localId, remoteId)
    }

    suspend fun getByRemoteId(remoteId: Int): TransactionEntity? {
        return transactionDao.getByRemoteId(remoteId)
    }

    suspend fun getUnsyncedCreationsOrUpdates(): List<TransactionEntity> {
        return transactionDao.getUnsyncedCreationsOrUpdates()
    }

    suspend fun getUnsyncedDeletions(): List<TransactionEntity> {
        return transactionDao.getUnsyncedDeletions()
    }

    suspend fun markAsDeleted(remoteId: Int) {
        transactionDao.markAsDeleted(remoteId)
    }

    suspend fun deletePermanently(remoteId: Int) {
        transactionDao.deletePermanently(remoteId)
    }

    suspend fun deleteByRemoteIds(remoteIds: List<Int>) {
        if (remoteIds.isNotEmpty()) {
            transactionDao.deleteByRemoteIds(remoteIds)
        }
    }

    suspend fun deleteAllForAccount(accountId: String) {
        transactionDao.deleteAllForAccount(accountId)
    }

    suspend fun getByLocalId(localId: Long): TransactionWithDetails? {
        return transactionDao.getByLocalId(localId)
    }

    suspend fun deleteByLocalId(localId: Long) {
        transactionDao.deleteByLocalId(localId)
    }
}