package com.coding.financialdetective.ui.screens.my_history_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.financialdetective.core.data.remote.RemoteTransactionDataSource
import com.coding.financialdetective.core.domain.repositories.TransactionDataSource
import com.coding.financialdetective.core.domain.util.onError
import com.coding.financialdetective.core.domain.util.onSuccess
import com.coding.financialdetective.core.networking.HttpClientFactory
import com.coding.financialdetective.core.presentation.util.formatNumberWithSpaces
import com.coding.financialdetective.mappers.toUiModel
import com.coding.financialdetective.models.domain_models.CategoryType
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

class MyHistoryViewModel : ViewModel() {
    private val _state = MutableStateFlow(MyHistoryScreenState())
    val state = _state.asStateFlow()
    private val transactionDataSource: TransactionDataSource = RemoteTransactionDataSource(
        HttpClientFactory.create(
            CIO.create()
        )
    )

    init {
        loadInitialTransactions(1)
    }

    fun loadInitialTransactions(accountId: Int) {
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        val startDate = startOfMonth.toString()
        val endDate = today.toString()

        loadTransactionsForPeriod(accountId = accountId, startDate = startDate, endDate = endDate)
    }

    fun loadTransactionsForPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val periodStartDate = try { LocalDate.parse(startDate) } catch (e: Exception) { LocalDate.now() }

            transactionDataSource
                .getTransactionsForPeriod(accountId, startDate, endDate)
                .onSuccess { transactions ->
                    val sortedTransactions = transactions.filter { it.category.type == CategoryType.EXPENSE  }.sortedByDescending { it.transactionDate }

                    val total = sortedTransactions.sumOf { it.amount }

                    val listItems = sortedTransactions.map { it.toUiModel() }

                    val formattedStart = periodStartDate.format(
                        DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru"))
                    ).replaceFirstChar { it.titlecase(Locale("ru")) }

                    val currentTime = LocalDateTime.now()
                    val formattedEnd = currentTime.format(DateTimeFormatter.ofPattern("HH:mm", Locale("ru")))

                    _state.update {
                        it.copy(
                            isLoading = false,
                            listItems = listItems,
                            totalAmount = formatNumberWithSpaces(total) + " ₽",
                            periodStart = formattedStart,
                            periodEnd = formattedEnd
                        )
                    }
                }
                .onError {

                }
        }
    }
}