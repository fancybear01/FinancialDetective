package com.coding.financialdetective.ui.screens.categories_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.models.domain_models.CategoryModelOld
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

    private fun getFakeSpendingItems(): List<CategoryModelOld> {
        return listOf(
            CategoryModelOld("1", "Аренда квартиры", "\uD83C\uDFE1"),
            CategoryModelOld("2", "Одежда", "\uD83D\uDC57"),
            CategoryModelOld("3", "На собаку", "\uD83D\uDC36"),
            CategoryModelOld("4", "Спортзал", "\uD83C\uDFCB\uFE0F\u200D♂\uFE0F"),
            CategoryModelOld("5", "Медицина", "\uD83D\uDC8A"),
            CategoryModelOld("6", "Продукты", "\uD83C\uDF6D"),
        )
    }
}