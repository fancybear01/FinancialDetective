package com.coding.feature_charts

import androidx.compose.ui.graphics.Color

/**
 * Модель данных для одного "куска" диаграммы.
 * @param value Абсолютное значение (например, сумма расходов).
 * @param color Цвет этого "куска".
 */
data class PieChartData(
    val value: Float,
    val color: Color
)
