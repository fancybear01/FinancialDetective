package com.coding.feature_transactions.ui.expenses_incomes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.data.util.onError
import com.coding.core.data.util.onSuccess
import com.coding.core_ui.util.toUiText
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.core.domain.model.categories_models.CategoryType
import com.coding.core.domain.model.transactions_models.Transaction
import com.coding.core.domain.model.transactions_models.TransactionType
import com.coding.core.domain.repository.TransactionRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class TransactionsViewModel @AssistedInject constructor(
    private val repository: TransactionRepository,
    @Assisted private val accountId: String,
    @Assisted private val transactionType: TransactionType,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(accountId: String, transactionType: TransactionType): TransactionsViewModel
    }

    private val _state = MutableStateFlow(TransactionsState())
    val state: StateFlow<TransactionsState> = _state.asStateFlow()

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