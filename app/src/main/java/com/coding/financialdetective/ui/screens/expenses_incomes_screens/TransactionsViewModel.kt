package com.coding.financialdetective.ui.screens.expenses_incomes_screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.core.data.remote.RemoteTransactionDataSource
import com.coding.financialdetective.core.domain.repositories.TransactionDataSource
import com.coding.financialdetective.core.domain.util.onError
import com.coding.financialdetective.core.domain.util.onSuccess
import com.coding.financialdetective.core.networking.HttpClientFactory
import com.coding.financialdetective.core.presentation.util.formatNumberWithSpaces
import com.coding.financialdetective.mappers.toUiModel
import com.coding.financialdetective.models.domain_models.CategoryType
import com.coding.financialdetective.models.domain_models.Transaction
import com.coding.financialdetective.models.domain_models.TransactionType
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class TransactionsViewModel(
    private val accountId: String,
    private val transactionType: TransactionType
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsState())
    val state: StateFlow<TransactionsState> = _state.asStateFlow()

    private val transactionDataSource: TransactionDataSource = RemoteTransactionDataSource(
        HttpClientFactory.create(
            CIO.create()
        )
    )

    init {
        loadTransactions(accountId)
    }

    private fun loadTransactions(accountId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val today = LocalDate.now().toString()
            var transactions = emptyList<Transaction>()
            transactionDataSource
                .getTransactionsForPeriod(accountId, today, today)
                .onSuccess { items ->
                    transactions = items
                }
                .onError {

                }

            when (transactionType) {
                TransactionType.EXPENSE -> {
                    val expenses = transactions.filter { it.category.type == CategoryType.EXPENSE  }
                    val total = expenses.sumOf { it.amount }
                    val uiModels = expenses.map { it.toUiModel() }

                    _state.value = TransactionsState(
                        totalAmount = total,
                        transactions = uiModels,
                        isLoading = false
                    )
                }
                TransactionType.INCOME -> {
                    val incomes = transactions.filter { it.category.type == CategoryType.INCOME  }
                    val total = incomes.sumOf { it.amount }
                    val uiModels = incomes.map { it.toUiModel() }

                    _state.value = TransactionsState(
                        totalAmount = total,
                        transactions = uiModels,
                        isLoading = false
                    )
                }
            }
        }
    }

}

class TransactionsViewModelFactory(
    private val accountId: String,
    private val transactionType: TransactionType
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionsViewModel(accountId, transactionType) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}