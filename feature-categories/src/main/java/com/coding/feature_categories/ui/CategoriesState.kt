package com.coding.feature_categories.ui

import com.coding.core.util.UiText
import com.coding.core_ui.model.CategoryUi

data class CategoriesState(
    val searchQuery: String = "",
    var listItems: List<CategoryUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiText? = null
)