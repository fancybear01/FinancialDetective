package com.coding.financialdetective.ui.screens.categories_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.models.domain_models.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {
    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()

    init {
        loadSpendingItems()
    }

    private fun loadSpendingItems() {
        viewModelScope.launch {
            val fakeSpendingItems = getFakeSpendingItems()

            _state.value = CategoriesState(
                searchQuery = "",
                results = fakeSpendingItems,
                isLoading = false
            )
        }
    }

    private fun getFakeSpendingItems(): List<Category> {
        return listOf(
            Category("1", "Аренда квартиры", "\uD83C\uDFE1"),
            Category("2", "Одежда", "\uD83D\uDC57"),
            Category("3", "На собаку", "\uD83D\uDC36"),
            Category("4", "Спортзал", "\uD83C\uDFCB\uFE0F\u200D♂\uFE0F"),
            Category("5", "Медицина", "\uD83D\uDC8A"),
            Category("6", "Продукты", "\uD83C\uDF6D"),
        )
    }
}