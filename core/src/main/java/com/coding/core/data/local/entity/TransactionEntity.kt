package com.coding.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    indices = [Index(value = ["remoteId"], unique = true)]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val remoteId: Int?,
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val transactionDate: String,
    val comment: String,
    val createdAt: String,
    val updatedAt: String,
    // Поля для синхронизации
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false,
    val clientLastUpdatedAt: Long = System.currentTimeMillis()
)