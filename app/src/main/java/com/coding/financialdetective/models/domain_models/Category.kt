package com.coding.financialdetective.models.domain_models

data class Category(
    val id: Int,
    val name: String,
    val emoji: String,
    val type: CategoryType
)