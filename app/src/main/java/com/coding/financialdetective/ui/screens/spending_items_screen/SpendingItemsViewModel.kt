package com.coding.financialdetective.ui.screens.spending_items_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.models.SpendingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SpendingItemsViewModel : ViewModel() {
    private val _state = MutableStateFlow(SpendingItemsState())
    val state: StateFlow<SpendingItemsState> = _state.asStateFlow()

    init {
        loadSpendingItems()
    }

    private fun loadSpendingItems() {
        viewModelScope.launch {
            val fakeSpendingItems = getFakeSpendingItems()

            _state.value = SpendingItemsState(
                searchQuery = "",
                results = fakeSpendingItems,
                isLoading = false
            )
        }
    }

    private fun getFakeSpendingItems(): List<SpendingItem> {
        return listOf(
            SpendingItem("1", "Аренда квартиры", "\uD83C\uDFE1"),
            SpendingItem("2", "Одежда", "\uD83D\uDC57"),
            SpendingItem("3", "На собаку", "\uD83D\uDC36"),
            SpendingItem("4", "Спортзал", "\uD83C\uDFCB\uFE0F\u200D♂\uFE0F"),
            SpendingItem("5", "Медицина", "\uD83D\uDC8A"),
            SpendingItem("6", "Продукты", "\uD83C\uDF6D"),
        )
    }
}