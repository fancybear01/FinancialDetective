package com.coding.feature_transactions.data.local.source

import com.coding.core.data.local.dao.TransactionDao
import com.coding.core.data.local.entity.TransactionEntity
import com.coding.core.data.local.relation.TransactionWithDetails
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TransactionLocalDataSource @Inject constructor(private val transactionDao: TransactionDao) {

    fun getTransactionsStream(accountId: String, startDate: String, endDate: String): Flow<List<TransactionWithDetails>> {
        return transactionDao.getTransactionsForPeriod(accountId, startDate, endDate)
    }

    suspend fun upsertTransactions(transactions: List<TransactionEntity>) {
        transactionDao.upsertAll(transactions)
    }

    suspend fun insertOrUpdate(transaction: TransactionEntity) {
        transactionDao.upsert(transaction)
    }

    suspend fun getUnsyncedTransactions(): List<TransactionEntity> {
        return transactionDao.getUnsynced()
    }

    suspend fun markAsUpdated(
        remoteId: Int, accountId: Int, categoryId: Int, transactionDate: ZonedDateTime,
        amount: Double, comment: String, timestamp: Long
    ) {
        transactionDao.markAsUpdated(
            remoteId = remoteId,
            accountId = accountId,
            categoryId = categoryId,
            transactionDate = transactionDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            amount = amount,
            comment = comment,
            timestamp = timestamp
        )
    }

    suspend fun markAsDeleted(remoteId: Int, timestamp: Long) {
        transactionDao.markAsDeleted(remoteId, timestamp)
    }

    suspend fun deleteLocalTransaction(localId: Long) {
        transactionDao.deleteByLocalId(localId)
    }

    suspend fun deleteSyncedTransactions(remoteIds: List<Int>) {
        transactionDao.deleteSynced(remoteIds)
    }
}