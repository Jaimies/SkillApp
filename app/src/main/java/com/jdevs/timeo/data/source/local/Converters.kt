package com.jdevs.timeo.data.source.local

import androidx.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

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
