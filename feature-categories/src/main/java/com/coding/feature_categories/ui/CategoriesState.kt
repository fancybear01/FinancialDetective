package com.coding.feature_categories.ui

import com.coding.core.util.UiText

data class CategoriesState(
    val searchQuery: String = "",
    var listItems: List<CategoryUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiText? = null
)