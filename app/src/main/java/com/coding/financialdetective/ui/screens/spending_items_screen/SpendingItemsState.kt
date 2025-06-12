package com.coding.financialdetective.ui.screens.spending_items_screen

import com.coding.financialdetective.models.SpendingItem

data class SpendingItemsState(
    val searchQuery: String = "",
    var results: List<SpendingItem> = emptyList(),
    val isLoading: Boolean = false
)