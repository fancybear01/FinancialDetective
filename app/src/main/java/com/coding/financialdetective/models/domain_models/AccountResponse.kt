package com.coding.financialdetective.models.domain_models

data class AccountResponse(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val incomeStats: List<StatItem>,
    val expenseStats: List<StatItem>,
    val createdAt: String,
    val updatedAt: String
)