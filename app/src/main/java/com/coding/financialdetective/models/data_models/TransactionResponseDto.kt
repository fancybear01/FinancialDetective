package com.coding.financialdetective.models.data_models

import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponseDto(
    val id: Int,
    val account: AccountBriefDto,
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String = "",
    val createdAt: String,
    val updatedAt: String
)