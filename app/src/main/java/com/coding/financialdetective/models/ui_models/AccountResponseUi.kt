package com.coding.financialdetective.models.ui_models

import com.coding.financialdetective.models.domain_models.StatItem

data class AccountResponseUi(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: List<StatItem>,
    val expenseStats: List<StatItem>,
    val createdAt: String,
    val updatedAt: String
)