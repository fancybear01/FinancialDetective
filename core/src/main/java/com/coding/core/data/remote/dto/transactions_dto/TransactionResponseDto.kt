package com.coding.core.data.remote.dto.transactions_dto

import com.coding.core.data.remote.dto.account_dto.AccountBriefDto
import com.coding.core.data.remote.dto.categoties_dto.CategoryDto
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