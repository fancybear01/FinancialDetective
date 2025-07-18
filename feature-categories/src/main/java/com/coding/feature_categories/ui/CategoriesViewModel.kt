package com.coding.feature_categories.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.data.util.onError
import com.coding.core.data.util.onSuccess
import com.coding.core_ui.util.toUiText
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.core.domain.model.categories_models.Category
import com.coding.core.domain.repository.CategoryRepository
import com.coding.core_ui.model.CategoryUi
import com.coding.core_ui.model.mapper.toUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    private val repository: CategoryRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()

    private val categoriesFromDb: Flow<List<Category>> = repository.getCategoriesStream()

    init {
        syncCategories()

        observeCategoriesAndSearch()

        observeConnectivity()
    }

    private fun observeConnectivity() {
        connectivityObserver.isConnected
            .drop(1)
            .filter { it }
            .debounce(1000)
            .onEach { syncCategories() }
            .launchIn(viewModelScope)
    }

    private fun observeCategoriesAndSearch() {
        viewModelScope.launch {
            combine(
                categoriesFromDb,
                state.map { it.searchQuery }.distinctUntilChanged()
            ) { categories, query ->

                val uiModels = categories.map { it.toUiModel() }

                val filteredList = if (query.isBlank()) {
                    uiModels
                } else {
                    uiModels.filter { categoryUi ->
                        categoryUi.name.contains(query, ignoreCase = true)
                    }
                }

                state.value.copy(
                    listItems = filteredList,
                    isLoading = false,
                    error = null
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun syncCategories(isRetry: Boolean = false) {
        viewModelScope.launch {
            if (_state.value.listItems.isEmpty() || isRetry) {
                _state.update { it.copy(isLoading = true, error = null) }
            }

            repository.syncCategories().onError { networkError ->
                if (_state.value.listItems.isEmpty()) {
                    _state.update { it.copy(error = networkError.toUiText()) }
                }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    fun retry() {
        syncCategories(isRetry = true)
    }
}