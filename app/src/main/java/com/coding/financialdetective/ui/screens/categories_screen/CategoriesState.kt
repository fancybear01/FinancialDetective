package com.coding.financialdetective.ui.screens.categories_screen

import com.coding.financialdetective.models.domain_models.Category

data class CategoriesState(
    val searchQuery: String = "",
    var results: List<Category> = emptyList(),
    val isLoading: Boolean = false
)