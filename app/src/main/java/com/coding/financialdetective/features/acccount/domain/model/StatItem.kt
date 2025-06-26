package com.coding.financialdetective.features.acccount.domain.model

data class StatItem(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: Double
)