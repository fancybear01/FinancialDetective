package com.coding.financialdetective.features.categories.ui

import com.coding.financialdetective.core_ui.util.UiText

data class CategoriesState(
    val searchQuery: String = "",
    var listItems: List<CategoryUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiText? = null
)