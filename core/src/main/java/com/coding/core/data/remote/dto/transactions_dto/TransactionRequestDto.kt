package com.coding.core.data.remote.dto.transactions_dto

import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequestDto(
    val accountId: Int,
    val categoryId: Int,
    val transactionDate: String,
    val amount: String,
    val comment: String
)