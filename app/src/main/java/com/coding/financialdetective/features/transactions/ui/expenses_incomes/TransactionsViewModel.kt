package com.coding.financialdetective.features.transactions.ui.expenses_incomes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.data.util.onError
import com.coding.financialdetective.data.util.onSuccess
import com.coding.financialdetective.core_ui.util.toUiText
import com.coding.financialdetective.features.categories.domain.model.CategoryType
import com.coding.financialdetective.features.transactions.domain.model.Transaction
import com.coding.financialdetective.features.transactions.domain.model.TransactionType
import com.coding.financialdetective.features.transactions.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class TransactionsViewModel(
    private val repository: TransactionRepository,
    private val accountId: String,
    private val transactionType: TransactionType
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsState())
    val state: StateFlow<TransactionsState> = _state.asStateFlow()

    init {
        loadTransactions(accountId)
    }

    private fun loadTransactions(accountId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

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

    fun retry() {
        loadTransactions(accountId)
    }
}