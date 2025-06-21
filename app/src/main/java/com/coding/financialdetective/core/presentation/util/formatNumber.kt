package com.coding.financialdetective.core.presentation.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.roundToLong

fun formatNumberWithSpaces(number: Double): String {
    val isInteger = number % 1 == 0.0

    val formatter = if (isInteger) {
        DecimalFormat("#,###")
    } else {
        DecimalFormat("#,###.##").apply {
            minimumFractionDigits = 1
            maximumFractionDigits = 2
        }
    }

    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    formatter.decimalFormatSymbols = symbols
    return formatter.format(number)
}