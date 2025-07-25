package com.coding.feature_charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Круговая диаграмма (Pie Chart).
 * @param data Список данных для отображения.
 * @param modifier Модификатор для настройки размера и отступов.
 * @param strokeWidth Ширина кольца диаграммы.
 */
@Composable
fun PieChart(
    data: List<PieChartData>,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 35.dp
) {
    val totalValue = remember(data) { data.sumOf { it.value.toDouble() }.toFloat() }

    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animatedProgress.snapTo(0f)
        launch {
            animatedProgress.animateTo(1f, animationSpec = tween(durationMillis = 750))
        }
    }

    val sweepAngles = remember(data, totalValue) {
        data.map { 360f * (it.value / totalValue) }
    }

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val canvasSize = constraints.maxWidth
        val strokePx = with(LocalDensity.current) { strokeWidth.toPx() }

        Canvas(
            modifier = Modifier.size(with(LocalDensity.current) { canvasSize.toDp() })
        ) {
            var startAngle = -90f

            sweepAngles.forEachIndexed { index, sweepAngle ->
                drawArc(
                    color = data[index].color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle * animatedProgress.value,
                    useCenter = false,
                    style = Stroke(width = strokePx, cap = StrokeCap.Butt)
                )
                startAngle += sweepAngle
            }
        }
    }
}