package com.coding.financialdetective.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.roundToLong

fun formatNumberWithSpaces(number: Double): String {
    val longValue = number.roundToLong()

    val formatter = DecimalFormat("#,###")

    val symbols = DecimalFormatSymbols(Locale.getDefault())
    symbols.groupingSeparator = ' '

    formatter.decimalFormatSymbols = symbols

    return formatter.format(longValue)
}