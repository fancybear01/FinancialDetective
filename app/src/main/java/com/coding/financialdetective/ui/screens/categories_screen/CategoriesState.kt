package com.coding.financialdetective.ui.screens.categories_screen

import com.coding.financialdetective.models.domain_models.CategoryModelOld

data class CategoriesState(
    val searchQuery: String = "",
    var results: List<CategoryModelOld> = emptyList(),
    val isLoading: Boolean = false
)