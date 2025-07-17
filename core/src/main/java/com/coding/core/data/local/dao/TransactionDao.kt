package com.coding.core.data.local.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.coding.core.data.local.entity.TransactionEntity
import com.coding.core.data.local.relation.TransactionWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(transactions: List<TransactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :remoteId LIMIT 1")
    suspend fun getByRemoteId(remoteId: Int): TransactionEntity?

    @Query("UPDATE transactions SET id = :remoteId, isSynced = 1 WHERE localId = :localId")
    suspend fun setSynced(localId: Long, remoteId: Int)

    @Query("DELETE FROM transactions WHERE id IN (:remoteIds)")
    suspend fun deleteByRemoteIds(remoteIds: List<Int>)

    @Transaction
    @Query("""
        SELECT * FROM transactions 
        WHERE accountId = :accountId 
        AND date(transactionDate) BETWEEN :startDate AND :endDate 
        AND isDeleted = 0 
        ORDER BY transactionDate DESC
    """)
    fun getTransactionsForPeriod(accountId: String, startDate: String, endDate: String): Flow<List<TransactionWithDetails>>

    @Query("SELECT * FROM transactions WHERE isSynced = 0 AND isDeleted = 0")
    suspend fun getUnsyncedCreationsOrUpdates(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE isSynced = 0 AND isDeleted = 1")
    suspend fun getUnsyncedDeletions(): List<TransactionEntity>

    @Query("UPDATE transactions SET isSynced = 0, isDeleted = 1 WHERE id = :remoteId")
    suspend fun markAsDeleted(remoteId: Int)

    @Query("DELETE FROM transactions WHERE id = :remoteId")
    suspend fun deletePermanently(remoteId: Int)
}