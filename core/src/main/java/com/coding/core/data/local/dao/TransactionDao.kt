package com.coding.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.coding.core.data.local.entity.TransactionEntity
import com.coding.core.data.local.relation.TransactionWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE isDeleted = 0 ORDER BY transactionDate DESC")
    fun getAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE isSynced = 0")
    suspend fun getUnsynced(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(transactions: List<TransactionEntity>)

    @Query("UPDATE transactions SET isDeleted = 1, isSynced = 0 WHERE remoteId = :id")
    suspend fun markAsDeleted(id: Int)

    @Transaction
    @Query("SELECT * FROM transactions WHERE accountId = :accountId AND transactionDate BETWEEN :startDate AND :endDate AND isDeleted = 0 ORDER BY transactionDate DESC")
    fun getTransactionsForPeriod(accountId: String, startDate: String, endDate: String): Flow<List<TransactionWithDetails>>

    @Query("UPDATE transactions SET remoteId = :remoteId, isSynced = 1 WHERE localId = :localId")
    suspend fun updateWithRemoteId(localId: Long, remoteId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(transaction: TransactionEntity)

    @Query("UPDATE transactions SET isSynced = 0, accountId = :accountId, categoryId = :categoryId, transactionDate = :transactionDate, amount = :amount, comment = :comment, clientLastUpdatedAt = :timestamp WHERE remoteId = :remoteId")
    suspend fun markAsUpdated(remoteId: Int, accountId: Int, categoryId: Int, transactionDate: String, amount: Double, comment: String, timestamp: Long)

    @Query("UPDATE transactions SET isDeleted = 1, isSynced = 0, clientLastUpdatedAt = :timestamp WHERE remoteId = :remoteId")
    suspend fun markAsDeleted(remoteId: Int, timestamp: Long)

    @Query("DELETE FROM transactions WHERE remoteId IN (:remoteIds)")
    suspend fun deleteSynced(remoteIds: List<Int>)

    @Query("DELETE FROM transactions WHERE localId = :localId")
    suspend fun deleteByLocalId(localId: Long)
}