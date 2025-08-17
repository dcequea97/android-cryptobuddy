package com.cequea.cryptobuddy.utils

import java.text.NumberFormat
import java.util.Locale

fun Double.getBalanceFormated(
    leadingText: String = "",
    trailingText: String = ""
): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
        isGroupingUsed = true
    }
    val numberFormatted = numberFormat.format(this)
    return "$leadingText$numberFormatted$trailingText"
}

fun Double.getCompatNumber(
    leadingText: String = "",
    trailingText: String = ""
): String {
    val suffixes = listOf("", "K", "M", "B", "T")
    var tempValue = this
    var suffixIndex = 0

    while (tempValue >= 1000 && suffixIndex < suffixes.size - 1) {
        tempValue /= 1000.0
        suffixIndex++
    }

    val numberFormatted = String.format(Locale.getDefault(), "%.3f%s", tempValue, suffixes[suffixIndex])
    return "$leadingText$numberFormatted$trailingText"
}