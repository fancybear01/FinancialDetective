package com.coding.financialdetective.features.transactions.ui.expenses_incomes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.data.util.onError
import com.coding.financialdetective.data.util.onSuccess
import com.coding.financialdetective.core_ui.util.toUiText
import com.coding.financialdetective.data.remote.connectivity.ConnectivityObserver
import com.coding.financialdetective.features.categories.domain.model.CategoryType
import com.coding.financialdetective.features.transactions.domain.model.Transaction
import com.coding.financialdetective.features.transactions.domain.model.TransactionType
import com.coding.financialdetective.features.transactions.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class TransactionsViewModel(
    private val repository: TransactionRepository,
    private val accountId: String,
    private val transactionType: TransactionType,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsState())
    val state: StateFlow<TransactionsState> = _state.asStateFlow()

    private val viewModelId = UUID.randomUUID().toString().substring(0, 5)

    private var lastUsedCurrency: String? = null

    init {
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

    private fun loadTransactions(accountId: String, currency: String) {
        this.lastUsedCurrency = currency

        viewModelScope.launch {

            val today = LocalDate.now().toString()
            var transactions = emptyList<Transaction>()

            repository
                .getTransactionsForPeriod(accountId, today, today)
                .onSuccess { items ->
                    transactions = items
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
            when (transactionType) {
                TransactionType.EXPENSE -> {
                    val expenses = transactions.filter { it.category.type == CategoryType.EXPENSE }
                    val total = expenses.sumOf { it.amount }
                    val uiModels = expenses.map { it.toUiModel() }

                    _state.update {
                        it.copy(
                            totalAmount = total,
                            transactions = uiModels,
                            isLoading = false
                        )
                    }
                }

                TransactionType.INCOME -> {
                    val incomes = transactions.filter { it.category.type == CategoryType.INCOME }
                    val total = incomes.sumOf { it.amount }
                    val uiModels = incomes.map { it.toUiModel() }

                    _state.update {
                        it.copy(
                            totalAmount = total,
                            transactions = uiModels,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun retry() {
        lastUsedCurrency?.let { currency ->
            loadTransactions(accountId, currency)
        }
    }

    fun retry(currency: String) {
        refresh(currency)
    }

    fun refresh(currency: String) {
        _state.update {
            it.copy(isLoading = true, currency = currency)
        }

        loadTransactions(accountId, currency)
    }
}