package com.coding.feature_categories.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.data.util.onError
import com.coding.core.data.util.onSuccess
import com.coding.core_ui.util.toUiText
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.core.domain.repository.CategoryRepository
import com.coding.core_ui.model.CategoryUi
import com.coding.core_ui.model.mapper.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    private val repository: CategoryRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {
    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()

    private var fullCategoriesList: List<CategoryUi> = emptyList()

    init {
        loadSpendingItems()
        observeConnectivity()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.isConnected
                .drop(1)
                .debounce(1000)
                .collect { connected ->
                    if (connected && state.value.error != null) {
                        retry()
                    }
                }
        }
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
                    return@launch
                }
        }
    }

    fun retry() {
        loadSpendingItems()
    }
}