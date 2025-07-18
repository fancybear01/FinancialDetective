package com.coding.feature_transactions.ui.analysis

import com.coding.core.domain.model.categories_models.CategoryAnalysisItem
import com.coding.core.util.UiText
import java.time.LocalDate

data class AnalysisState(
    val startDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val endDate: LocalDate = LocalDate.now(),
    val periodStartText: String = "",
    val periodEndText: String = "",
    val totalAmount: Double = 0.0,
    val categoryItems: List<CategoryAnalysisItem> = emptyList(),
    val currencyCode: String = "",
    val isLoading: Boolean = true,
    val error: UiText? = null,
    val noDataForAnalysis: Boolean = false
)