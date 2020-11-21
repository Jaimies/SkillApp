package com.jdevs.timeo.data.db

import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object Converters {

    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @JvmStatic
    @TypeConverter
    fun toOffsetDateTime(value: String): OffsetDateTime =
        formatter.parse(value, OffsetDateTime::from)

    @JvmStatic
    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime): String = date.format(formatter)
}
