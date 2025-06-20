package com.coding.financialdetective.ui.screens.my_history_screen

import com.coding.financialdetective.core.domain.util.NetworkError
import com.coding.financialdetective.models.ui_models.TransactionUi
import java.time.LocalDate

data class MyHistoryScreenState(
    val isLoading: Boolean = true,
    val listItems: List<TransactionUi> = emptyList(),
    val startDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val endDate: LocalDate = LocalDate.now(),
    val periodStart: String = "",
    val periodEnd: String = "",
    val totalAmount: String = "",
    val error: NetworkError? = null
)