package com.coding.financialdetective.features.acccount.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountDto(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
)