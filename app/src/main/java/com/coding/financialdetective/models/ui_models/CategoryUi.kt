package com.coding.financialdetective.models.ui_models

import com.coding.financialdetective.models.domain_models.CategoryType

data class CategoryUi(
    val id: Int,
    val name: String,
    val emoji: String,
    val type: CategoryType
)