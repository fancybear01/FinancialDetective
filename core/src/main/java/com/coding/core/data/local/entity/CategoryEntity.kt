package com.coding.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [Index(value = ["categoryId"], unique = true)]
)
data class CategoryEntity(
    @PrimaryKey val categoryId: Int,
    val name: String,
    val emoji: String,
    val type: String
)