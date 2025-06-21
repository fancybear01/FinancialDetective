package com.coding.financialdetective.models.data_models

import kotlinx.serialization.Serializable

@Serializable
data class StatItemDto(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: String
)