package com.coding.financialdetective.features.transactions.data.remote.dto

import com.coding.financialdetective.features.acccount.data.remote.dto.AccountBriefDto
import com.coding.financialdetective.features.categories.data.remote.dto.CategoryDto
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