package com.coding.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    indices = [Index(value = ["id"], unique = true)]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val id: Int?, // Серверный ID,

    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val transactionDate: String,
    val comment: String,
    val createdAt: String,
    val updatedAt: String,

    val isSynced: Boolean,
    val isDeleted: Boolean = false
)