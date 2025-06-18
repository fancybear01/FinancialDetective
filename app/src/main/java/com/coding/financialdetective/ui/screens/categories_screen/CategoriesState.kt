package com.coding.financialdetective.ui.screens.categories_screen

import com.coding.financialdetective.models.ui_models.CategoryUi

data class CategoriesState(
    val searchQuery: String = "",
    var listItems: List<CategoryUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)