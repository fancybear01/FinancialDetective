package com.coding.financialdetective.ui.screens.my_history_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.core.data.remote.RemoteTransactionDataSource
import com.coding.financialdetective.core.domain.repositories.TransactionDataSource
import com.coding.financialdetective.core.domain.util.onError
import com.coding.financialdetective.core.domain.util.onSuccess
import com.coding.financialdetective.core.networking.HttpClientFactory
import com.coding.financialdetective.core.presentation.util.formatNumberWithSpaces
import com.coding.financialdetective.core.presentation.util.toUiText
import com.coding.financialdetective.mappers.toUiModel
import com.coding.financialdetective.models.domain_models.CategoryType
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MyHistoryViewModel(
    private val accountId: String,
    transactionType: String,
) : ViewModel() {
    private val categoryTypeToLoad = if (transactionType.equals("income", ignoreCase = true)) {
        CategoryType.INCOME
    } else {
        CategoryType.EXPENSE
    }
    private val _state = MutableStateFlow(MyHistoryScreenState())
    val state = _state.asStateFlow()
    private val transactionDataSource: TransactionDataSource = RemoteTransactionDataSource(
        HttpClientFactory.create(
            CIO.create()
        )
    )

    init {
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        _state.update { it.copy(startDate = startOfMonth, endDate = today) }
        reloadData()
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
        val currentState = _state.value
        loadTransactionsForPeriod(
            accountId = accountId,
            startDate = currentState.startDate.toString(),
            endDate = currentState.endDate.toString()
        )
    }

    private fun loadTransactionsForPeriod(
        accountId: String,
        startDate: String,
        endDate: String
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, listItems = emptyList(), totalAmount = "") }

            val periodStartDate = try {
                LocalDate.parse(startDate)
            } catch (e: Exception) {
                LocalDate.now().withDayOfMonth(1)
            }
            val periodEndDate = try {
                LocalDate.parse(endDate)
            } catch (e: Exception) {
                LocalDate.now()
            }

            transactionDataSource
                .getTransactionsForPeriod(accountId, startDate, endDate)
                .onSuccess { transactions ->
                    val sortedTransactions = transactions
                        .filter { it.category.type == categoryTypeToLoad }
                        .sortedBy { it.transactionDate }

                    val total = sortedTransactions.sumOf { it.amount }

                    val listItems = sortedTransactions.map { it.toUiModel() }

                    val startFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale("ru"))
                    val formattedStart = periodStartDate.format(startFormatter)

                    val formattedEnd = if (periodEndDate.isEqual(LocalDate.now())) {
                        LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale("ru")))
                    } else {
                        periodEndDate.format(startFormatter)
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            listItems = listItems,
                            totalAmount = formatNumberWithSpaces(total) + " ₽",
                            periodStart = formattedStart,
                            periodEnd = formattedEnd,
                            startDate = periodStartDate,
                            endDate = periodEndDate
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

class MyHistoryViewModelFactory(
    private val accountId: String,
    private val transactionType: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyHistoryViewModel(accountId, transactionType) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}