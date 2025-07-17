package com.coding.feature_transactions.data.local.source

import android.util.Log
import com.coding.core.data.local.dao.TransactionDao
import com.coding.core.data.local.entity.TransactionEntity
import com.coding.core.data.local.relation.TransactionWithDetails
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TransactionLocalDataSource @Inject constructor(private val transactionDao: TransactionDao) {

    fun getTransactionsStream(accountId: String, startDate: String, endDate: String): Flow<List<TransactionWithDetails>> {
        Log.d("DB_WRITE", "LOCAL_SOURCE: getTransactionsStream called for accountId=$accountId, startDate=$startDate, endDate=$endDate")
        return transactionDao.getTransactionsForPeriod(accountId, startDate, endDate)
    }

    suspend fun upsertAll(transactions: List<TransactionEntity>) {
        Log.d("DB_WRITE", "LOCAL_SOURCE: upsertAll called with ${transactions.size} items.")
        transactionDao.upsertAll(transactions)
    }

    suspend fun upsert(transaction: TransactionEntity) {
        Log.d("DB_WRITE", "LOCAL_SOURCE: upsert(single) called for remoteId=${transaction.id}, localId=${transaction.localId}")
        transactionDao.upsert(transaction)
    }

    suspend fun setSynced(localId: Long, remoteId: Int) {
        Log.d("DB_WRITE", "LOCAL_SOURCE: setSynced called for localId=$localId, remoteId=$remoteId")
        transactionDao.setSynced(localId, remoteId)
    }

    suspend fun getByRemoteId(remoteId: Int): TransactionEntity? {
        Log.d("DB_WRITE", "LOCAL_SOURCE: getByRemoteId called with remoteId = $remoteId")
        return transactionDao.getByRemoteId(remoteId)
    }

    suspend fun getUnsyncedCreationsOrUpdates(): List<TransactionEntity> {
        Log.d("DB_WRITE", "LOCAL_SOURCE: getUnsyncedCreationsOrUpdates called")
        return transactionDao.getUnsyncedCreationsOrUpdates()
    }

    suspend fun getUnsyncedDeletions(): List<TransactionEntity> {
        Log.d("DB_WRITE", "LOCAL_SOURCE: getUnsyncedDeletions called")
        return transactionDao.getUnsyncedDeletions()
    }

    suspend fun markAsDeleted(remoteId: Int) {
        Log.d("DB_WRITE", "LOCAL_SOURCE: markAsDeleted called")
        transactionDao.markAsDeleted(remoteId)
    }

    suspend fun deletePermanently(remoteId: Int) {
        Log.d("DB_WRITE", "LOCAL_SOURCE: deletePermanently called")
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
}