package com.coding.financialdetective.util

import androidx.room.Database
import androidx.room.RoomDatabase
import com.coding.core.data.local.dao.AccountDao
import com.coding.core.data.local.dao.CategoryDao
import com.coding.core.data.local.dao.TransactionDao
import com.coding.core.data.local.entity.AccountEntity
import com.coding.core.data.local.entity.CategoryEntity
import com.coding.core.data.local.entity.TransactionEntity

@Database(
    entities = [AccountEntity::class, CategoryEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
}