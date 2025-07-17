package com.coding.feature_transactions.ui.my_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.core.data.util.onError
import com.coding.core.util.formatNumberWithSpaces
import com.coding.core.data.remote.connectivity.ConnectivityObserver
import com.coding.core.domain.model.categories_models.CategoryType
import com.coding.core.domain.model.transactions_models.Transaction
import com.coding.core.domain.repository.TransactionRepository
import com.coding.core_ui.model.mapper.toUiModel
import com.coding.core_ui.util.toUiText
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
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

    private val categoryTypeToLoad = if (isIncome) CategoryType.INCOME else CategoryType.EXPENSE

    private val _startDate = MutableStateFlow(LocalDate.now().withDayOfMonth(1))
    private val _endDate = MutableStateFlow(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val transactionsFromDb: Flow<List<Transaction>> = combine(_startDate, _endDate) { start, end ->
        start.toString() to end.toString()
    }.flatMapLatest { (start, end) ->
        repository.getTransactionsStream(accountId, start, end)
    }

    val state: StateFlow<MyHistoryState> = combine(
        transactionsFromDb,
        _startDate,
        _endDate
    ) { transactions, startDate, endDate ->
        val filtered = transactions.filter { it.category.type == categoryTypeToLoad }
        val total = filtered.sumOf { it.amount }
        val listItems = filtered.map { it.toUiModel() }

        MyHistoryState(
            listItems = listItems,
            totalAmount = formatNumberWithSpaces(total),
            periodStart = startDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))),
            periodEnd = endDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))),
            startDate = startDate,
            endDate = endDate,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MyHistoryState())

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        syncData()
        observeConnectivity()
    }

    private fun syncData() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.syncTransactionsForPeriod(
                accountId,
                _startDate.value.toString(),
                _endDate.value.toString()
            ).onError {

            }
            _isLoading.value = false
        }
    }

    private fun observeConnectivity() {
        connectivityObserver.isConnected
            .drop(1).filter { it }
            .debounce(1000)
            .onEach { syncData() }
            .launchIn(viewModelScope)
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