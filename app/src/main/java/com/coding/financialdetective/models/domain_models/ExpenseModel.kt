package com.coding.financialdetective.models.domain_models

data class Expense(
    val id: String,
    val category: String,
    val subcategory: String? = null,
    val amount: Double,
    val emoji: String
)
