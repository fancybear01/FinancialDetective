package com.coding.financialdetective.models

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Expense(
    val id: String,
    val category: String,
    val subcategory: String? = null,
    val amount: Double,
    val emoji: String
)
