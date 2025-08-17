package com.cequea.cryptobuddy.utils

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    @TypeConverter
    fun fromString(value: String?): LocalDateTime? =
        value?.let { LocalDateTime.parse(it, formatter) }

    @TypeConverter
    fun toString(date: LocalDateTime?): String? =
        date?.format(formatter)
}