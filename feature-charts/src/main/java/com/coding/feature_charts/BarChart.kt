package com.coding.feature_charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class BarChartEntry(
    val date: LocalDate,
    val income: Float,
    val expense: Float
)

@OptIn(ExperimentalTextApi::class)
@Composable
fun BarChart(
    entries: List<BarChartEntry>,
    modifier: Modifier = Modifier,
    barColorIncome: Color = Color(0xFF4CAF50),
    barColorExpense: Color = Color(0xFFFF5722)
) {
    val animatedProgress = remember { entries.map { Animatable(0f) } }
    LaunchedEffect(entries) {
        animatedProgress.forEachIndexed { index, animatable ->
            launch {
                animatable.snapTo(0f)
                animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 300, delayMillis = index * 20)
                )
            }
        }
    }
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(fontSize = 10.sp, color = Color.Gray)
    val formatter = remember { DateTimeFormatter.ofPattern("dd.MM") }

    Canvas(modifier = modifier) {
        if (entries.isEmpty()) return@Canvas

        val maxAmount = entries.maxOfOrNull { maxOf(it.income, it.expense) } ?: 1f
        val chartAreaHeight = size.height - 20.dp.toPx()

        val slotWidth = size.width / entries.size
        val barWidth = slotWidth * 0.4f
        val spaceBetweenBars = slotWidth * 0.1f

        entries.forEachIndexed { index, entry ->
            val slotStart = index * slotWidth

            if (entry.income > 0) {
                val barHeight = (entry.income / maxAmount) * chartAreaHeight
                drawRoundRect(
                    color = barColorIncome,
                    topLeft = Offset(
                        x = slotStart,
                        y = chartAreaHeight - barHeight * animatedProgress[index].value
                    ),
                    size = Size(barWidth, barHeight * animatedProgress[index].value),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
            }

            if (entry.expense > 0) {
                val barHeight = (entry.expense / maxAmount) * chartAreaHeight
                drawRoundRect(
                    color = barColorExpense,
                    topLeft = Offset(
                        x = slotStart + barWidth + spaceBetweenBars,
                        y = chartAreaHeight - barHeight * animatedProgress[index].value
                    ),
                    size = Size(barWidth, barHeight * animatedProgress[index].value),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
            }
        }

        val startIndex = 0
        val midIndex = if (entries.size > 1) entries.size / 2 else -1
        val endIndex = if (entries.isNotEmpty()) entries.size - 1 else -1

        val labels = mutableMapOf<Int, String>()
        if (startIndex != -1) labels[startIndex] = entries.first().date.format(formatter)
        if (midIndex != -1) labels[midIndex] = entries[midIndex].date.format(formatter)
        if (endIndex != -1) labels[endIndex] = entries.last().date.format(formatter)

        labels.forEach { (index, text) ->
            val slotStart = index * slotWidth
            val textLayoutResult = textMeasurer.measure(AnnotatedString(text), style = labelStyle)
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = slotStart + (slotWidth / 2) - (textLayoutResult.size.width / 2),
                    y = size.height - textLayoutResult.size.height
                )
            )
        }
    }
}