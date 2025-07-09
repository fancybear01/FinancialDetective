package com.coding.core.data.dto.account_dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountResponseDto(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: List<StatItemDto>,
    val expenseStats: List<StatItemDto>,
    val createdAt: String,
    val updatedAt: String
)