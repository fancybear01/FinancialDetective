package com.coding.financialdetective.features.categories.ui

import com.coding.financialdetective.features.categories.domain.model.CategoryType


data class CategoryUi(
    val id: Int,
    val name: String,
    val emoji: String,
    val type: CategoryType
)