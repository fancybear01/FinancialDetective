package com.coding.financialdetective.features.acccount.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StatItemDto(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: String
)