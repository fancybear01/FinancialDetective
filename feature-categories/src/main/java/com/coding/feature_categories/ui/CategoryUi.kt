package com.coding.feature_categories.ui

import com.coding.core.domain.model.categories_models.CategoryType


data class CategoryUi(
    val id: Int,
    val name: String,
    val emoji: String,
    val type: CategoryType
)