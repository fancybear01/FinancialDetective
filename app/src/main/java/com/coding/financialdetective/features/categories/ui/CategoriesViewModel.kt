package com.coding.financialdetective.features.categories.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.data.util.onError
import com.coding.financialdetective.data.util.onSuccess
import com.coding.financialdetective.core_ui.util.toUiText
import com.coding.financialdetective.features.categories.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val repository: CategoryRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()

    private var fullCategoriesList: List<CategoryUi> = emptyList()

    init {
        loadSpendingItems()
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }

        val filteredList = if (query.isBlank()) {
            fullCategoriesList
        } else {
            fullCategoriesList.filter { category ->
                category.name.contains(query, ignoreCase = true)
            }
        }

        _state.update { it.copy(listItems = filteredList) }
    }

    private fun loadSpendingItems() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository
                .getCategories()
                .onSuccess { categories ->
                    val listItems = categories.map { it.toUiModel() }
                    fullCategoriesList = listItems
                    _state.update {
                        it.copy(
                            listItems = listItems,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onError { networkError ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = networkError.toUiText()
                        )
                    }
                }
        }
    }

    fun retry() {
        loadSpendingItems()
    }
}