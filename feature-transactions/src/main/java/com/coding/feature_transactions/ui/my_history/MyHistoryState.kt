package com.coding.feature_transactions.ui.my_history

import com.coding.core.util.UiText
import com.coding.feature_transactions.ui.model.TransactionUi
import java.time.LocalDate

data class MyHistoryState(
    val isLoading: Boolean = false,
    val listItems: List<TransactionUi> = emptyList(),
    val startDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val endDate: LocalDate = LocalDate.now(),
    val periodStart: String = "",
    val periodEnd: String = "",
    val totalAmount: String = "",
    val currencyCode: String = "",
    val error: UiText? = null
)