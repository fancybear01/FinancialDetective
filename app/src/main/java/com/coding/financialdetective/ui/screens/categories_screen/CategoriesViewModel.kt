package com.coding.financialdetective.ui.screens.categories_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.core.data.remote.RemoteCategoriesDataSource
import com.coding.financialdetective.core.domain.repositories.CategoriesDataSource
import com.coding.financialdetective.core.domain.util.onError
import com.coding.financialdetective.core.domain.util.onSuccess
import com.coding.financialdetective.core.networking.HttpClientFactory
import com.coding.financialdetective.mappers.toUiModel
import com.coding.financialdetective.models.ui_models.CategoryUi
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {
    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()
    private val categoriesDataSource: CategoriesDataSource = RemoteCategoriesDataSource(
        HttpClientFactory.create(
            CIO.create()
        )
    )
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

            categoriesDataSource
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
                .onError {  }
        }
    }
}