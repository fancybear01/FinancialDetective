package com.coding.financialdetective.ui.screens.my_history_screen

import com.coding.financialdetective.core.domain.util.NetworkError
import com.coding.financialdetective.models.ui_models.TransactionUi

data class MyHistoryScreenState(
    val isLoading: Boolean = true,
    val listItems: List<TransactionUi> = emptyList(),
    val periodStart: String = "",
    val periodEnd: String = "",
    val totalAmount: String = "",
    val error: NetworkError? = null
)