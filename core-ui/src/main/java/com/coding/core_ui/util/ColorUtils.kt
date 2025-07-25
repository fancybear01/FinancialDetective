package com.coding.core_ui.util

import androidx.compose.ui.graphics.Color

fun generateRandomColors(count: Int): List<Color> {
    val colors = mutableListOf<Color>()
    val random = java.util.Random()
    for (i in 0 until count) {
        val hue = (360f / count) * i
        colors.add(Color.hsv(hue, 0.7f, 0.85f))
    }
    return colors
}