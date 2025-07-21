package com.coding.feature_transactions.ui.analysis

import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.data.util.onError
import com.coding.core.domain.model.account_models.Currency
import com.coding.core.domain.model.categories_models.CategoryAnalysisItem
import com.coding.core.domain.model.categories_models.CategoryType
import com.coding.core.domain.model.transactions_models.Transaction
import com.coding.core.domain.repository.TransactionRepository
import com.coding.core.util.UiText
import com.coding.core.util.formatNumberWithSpaces
import com.coding.core_ui.util.generateRandomColors
import com.coding.core_ui.util.toUiText
import com.coding.feature_charts.ChartLegendItem
import com.coding.feature_charts.PieChartData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class AnalysisViewModel @AssistedInject constructor(
    private val repository: TransactionRepository,
    @Assisted private val accountId: String,
    @Assisted private val isIncome: Boolean,
    // ...
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(accountId: String, isIncome: Boolean): AnalysisViewModel
    }

    private val categoryTypeToLoad = if (isIncome) CategoryType.INCOME else CategoryType.EXPENSE

    private val _startDate = MutableStateFlow(LocalDate.now().withDayOfMonth(1))
    private val _endDate = MutableStateFlow(LocalDate.now())

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<UiText?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val transactionsFromDb: Flow<List<Transaction>> = combine(_startDate, _endDate) { start, end ->
        start.toString() to end.toString()
    }.flatMapLatest { (start, end) ->
        repository.getTransactionsStream(accountId, start, end)
    }

    val state: StateFlow<AnalysisState> = combine(
        transactionsFromDb, _startDate, _endDate, _error
    ) { transactions, startDate, endDate, error ->
        val filtered = transactions.filter { it.category.type == categoryTypeToLoad }
        val periodTotal = filtered.sumOf { it.amount }

        val categoryItems = filtered
            .groupBy { it.category }
            .map { (category, transactionList) ->
                val categoryTotal = transactionList.sumOf { it.amount }
                val percentage = if (periodTotal > 0) (categoryTotal / periodTotal).toFloat() * 100 else 0f
                CategoryAnalysisItem(
                    category = category,
                    totalAmount = categoryTotal,
                    percentage = percentage
                )
            }
            .sortedByDescending { it.totalAmount }
        val noData = categoryItems.isEmpty()
        val colors = generateRandomColors(categoryItems.size)
        val chartData = categoryItems.mapIndexed { index, item ->
            PieChartData(value = item.totalAmount.toFloat(), color = colors[index])
        }
        val chartLegend = categoryItems.mapIndexed { index, item ->
            ChartLegendItem(
                color = colors[index],
                label = item.category.name
            )
        }
        AnalysisState(
            startDate = startDate,
            endDate = endDate,
            periodStartText = startDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))),
            periodEndText = endDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))),
            totalAmount = periodTotal,
            categoryItems = categoryItems,
            currencyCode = transactions.firstOrNull()?.account?.currency ?: "RUB",
            noDataForAnalysis = noData,
            chartData = chartData,
            chartLegend = chartLegend,
            error = error
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnalysisState())

    private fun syncData() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.syncTransactionsForPeriod(
                accountId,
                _startDate.value.toString(),
                _endDate.value.toString()
            ).onError { networkError ->
                _error.value = networkError.toUiText()
            }
            _isLoading.value = false
        }
    }

    fun updateStartDate(newStartDate: LocalDate) {
        _startDate.value = newStartDate
        syncData()
    }

    fun updateEndDate(newEndDate: LocalDate) {
        _endDate.value = newEndDate
        syncData()
    }

    fun onRefresh() {
        syncData()
    }
}