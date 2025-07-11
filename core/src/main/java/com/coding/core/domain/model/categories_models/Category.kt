package com.coding.core.domain.model.categories_models

data class Category(
    val id: Int,
    val name: String,
    val emoji: String,
    val type: CategoryType
)