package com.coding.feature_transactions.ui.my_history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.data.util.onError
import com.coding.core.data.util.onSuccess
import com.coding.core.util.formatNumberWithSpaces
import com.coding.core_ui.util.toUiText
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.core.domain.model.categories_models.CategoryType
import com.coding.core.domain.repository.TransactionRepository
import com.coding.core_ui.model.mapper.toUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MyHistoryViewModel @AssistedInject constructor(
    private val repository: TransactionRepository,
    @Assisted private val accountId: String,
    @Assisted private val isIncome: Boolean,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(accountId: String, isIncome: Boolean): MyHistoryViewModel
    }

    private val categoryTypeToLoad = if (isIncome) {
        CategoryType.INCOME
    } else {
        CategoryType.EXPENSE
    }

    private val _state = MutableStateFlow(MyHistoryState())
    val state = _state.asStateFlow()

    init {
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        _state.update { it.copy(startDate = startOfMonth, endDate = today) }
        observeConnectivity()
    }

    fun onAccountUpdated(currencyCode: String) {
        loadData(newCurrency = currencyCode)
    }

    fun updateStartDate(newStartDate: LocalDate) {
        loadData(newStartDate = newStartDate)
    }

    fun updateEndDate(newEndDate: LocalDate) {
        loadData(newEndDate = newEndDate)
    }

    fun retry(currencyCode: String) {
        loadData(newCurrency = currencyCode)
    }

    private fun loadData(
        newStartDate: LocalDate = state.value.startDate,
        newEndDate: LocalDate = state.value.endDate,
        newCurrency: String = state.value.currencyCode
    ) {
        if (newCurrency.isBlank()) {
            return
        }
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    startDate = newStartDate,
                    endDate = newEndDate,
                    currencyCode = newCurrency
                )
            }

            val currentState = state.value

            repository
                .getTransactionsForPeriod(
                    accountId,
                    currentState.startDate.toString(),
                    currentState.endDate.toString()
                )
                .onSuccess { transactions ->
                    val sortedTransactions = transactions
                        .filter { it.category.type == categoryTypeToLoad }
                        .sortedByDescending { it.transactionDate }

                    val total = sortedTransactions.sumOf { it.amount }
                    val listItems = sortedTransactions.map { it.toUiModel() }

                    val startFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
                    val endFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
                    val formattedStart = currentState.startDate.format(startFormatter)
                    val formattedEnd = currentState.endDate.format(endFormatter)

                    _state.update {
                        it.copy(
                            isLoading = false,
                            listItems = listItems,
                            totalAmount = formatNumberWithSpaces(total),
                            periodStart = formattedStart,
                            periodEnd = formattedEnd
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


    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.isConnected
                .drop(1)
                .debounce(1000)
                .collect { connected ->
                    if (connected && state.value.error != null) {
                        retry(state.value.currencyCode)
                    }
                }
        }
    }
}