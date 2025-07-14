package com.coding.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "accounts",
    indices = [Index(value = ["accountId"], unique = true)]
)
data class AccountEntity(
    @PrimaryKey val accountId: String,
    val userId: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
)