package com.coding.financialdetective.features.transactions.ui.my_history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.data.util.onError
import com.coding.financialdetective.data.util.onSuccess
import com.coding.financialdetective.core_ui.util.formatNumberWithSpaces
import com.coding.financialdetective.core_ui.util.toUiText
import com.coding.financialdetective.data.remote.connectivity.ConnectivityObserver
import com.coding.financialdetective.features.categories.domain.model.CategoryType
import com.coding.financialdetective.features.transactions.domain.repository.TransactionRepository
import com.coding.financialdetective.features.transactions.ui.expenses_incomes.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MyHistoryViewModel(
    private val repository: TransactionRepository,
    private val accountId: String,
    savedStateHandle: SavedStateHandle,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val isIncome: Boolean = savedStateHandle.get<Boolean>("isIncome") ?: false

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
        reloadData()
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

    fun updateStartDate(newStartDate: LocalDate) {
        _state.update { it.copy(startDate = newStartDate) }
        reloadData()
    }

    fun updateEndDate(newEndDate: LocalDate) {
        _state.update { it.copy(endDate = newEndDate) }
        reloadData()
    }

    private fun reloadData() {
        loadTransactionsForPeriod(
            accountId = accountId,
            startDate = _state.value.startDate,
            endDate = _state.value.endDate
        )
    }

    private fun loadTransactionsForPeriod(
        accountId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository
                .getTransactionsForPeriod(accountId, startDate.toString(), endDate.toString())
                .onSuccess { transactions ->
                    val sortedTransactions = transactions
                        .filter { it.category.type == categoryTypeToLoad }
                        .sortedByDescending { it.transactionDate }

                    val total = sortedTransactions.sumOf { it.amount }
                    val listItems = sortedTransactions.map { it.toUiModel() }

                    val startFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
                    val endFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
                    val formattedStart = startDate.format(startFormatter)
                    val formattedEnd = endDate.format(endFormatter)

                    _state.update {
                        it.copy(
                            isLoading = false,
                            listItems = listItems,
                            totalAmount = formatNumberWithSpaces(total) + " ₽",
                            periodStart = formattedStart,
                            periodEnd = formattedEnd,
                            startDate = startDate,
                            endDate = endDate
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
        reloadData()
    }
}