package com.coding.feature_transactions.ui.expenses_incomes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.data.util.onError
import com.coding.core_ui.util.toUiText
import com.coding.core.domain.model.account_models.Account
import com.coding.core.domain.model.categories_models.CategoryType
import com.coding.core.domain.model.transactions_models.TransactionType
import com.coding.core.domain.repository.TransactionRepository
import com.coding.core_ui.model.mapper.toUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class TransactionsViewModel @AssistedInject constructor(
    private val repository: TransactionRepository,
    @Assisted private val accountId: String,
    @Assisted private val transactionType: TransactionType,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(accountId: String, transactionType: TransactionType): TransactionsViewModel
    }

    private val _state = MutableStateFlow(TransactionsState())
    val state: StateFlow<TransactionsState> = _state.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing = _isSyncing.asStateFlow()

    init {
        observeTransactions()
    }

    private fun observeTransactions() {
        val today = LocalDate.now().toString()
        val startDate = today
        val endDate = today

        repository.getTransactionsStream(accountId, startDate, endDate)
            .onEach { transactions ->
                val filteredByType = transactions.filter {
                    it.category.type == if (transactionType == TransactionType.INCOME) CategoryType.INCOME else CategoryType.EXPENSE
                }

                val total = filteredByType.sumOf { it.amount }
                val uiModels = filteredByType.map { it.toUiModel() }

                _state.update {
                    it.copy(
                        totalAmount = total,
                        transactions = uiModels,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun syncTransactions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val now = LocalDate.now()
            val startDate = now.withDayOfMonth(1).toString()
            val endDate = now.with(TemporalAdjusters.lastDayOfMonth()).toString()
            repository.syncTransactionsForPeriod(accountId, startDate, endDate)
                .onError { networkError ->
                    if (state.value.transactions.isEmpty()) {
                        _state.update { it.copy(error = networkError.toUiText()) }
                    }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun updateAccount(account: Account) {
        _state.update { it.copy(currency = account.currency) }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _isSyncing.value = true
            val today = LocalDate.now().toString()
            val startDate = today
            val endDate = today
            repository.syncTransactionsForPeriod(accountId, startDate, endDate)
            _isSyncing.value = false
        }
    }
}