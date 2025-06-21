package com.coding.financialdetective.models.domain_models

data class StatItem(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: Double
)