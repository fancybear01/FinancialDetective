package com.coding.feature_charts

import java.time.LocalDate

data class DailyChartData(
    val date: LocalDate,
    val totalIncome: Double,
    val totalExpense: Double
)