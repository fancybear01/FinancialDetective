package com.coding.financialdetective.features.categories.domain.model

data class Category(
    val id: Int,
    val name: String,
    val emoji: String,
    val type: CategoryType
)