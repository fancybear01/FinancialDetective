package com.coding.financialdetective.features.transactions.ui.my_history

import com.coding.financialdetective.core_ui.util.UiText
import com.coding.financialdetective.features.transactions.ui.model.TransactionUi
import java.time.LocalDate

data class MyHistoryState(
    val isLoading: Boolean = true,
    val listItems: List<TransactionUi> = emptyList(),
    val startDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val endDate: LocalDate = LocalDate.now(),
    val periodStart: String = "",
    val periodEnd: String = "",
    val totalAmount: String = "",
    val error: UiText? = null
)