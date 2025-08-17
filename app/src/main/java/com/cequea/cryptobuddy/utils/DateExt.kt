package com.cequea.cryptobuddy.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.getDateFormatted(): String {
    val userZone = ZoneId.systemDefault()
    val locale = Locale.getDefault()

    val zonedDate = this.atZone(userZone)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss", locale)

    return zonedDate.format(formatter)
}
