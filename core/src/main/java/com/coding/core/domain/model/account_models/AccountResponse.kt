package com.coding.core.domain.model.account_models

data class AccountResponse(
    val id: String,
    val name: String,
    val balance: Double,
    val currency: String,
    val incomeStats: List<StatItem>,
    val expenseStats: List<StatItem>,
    val createdAt: String,
    val updatedAt: String
)