package com.coding.financialdetective.features.categories.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)