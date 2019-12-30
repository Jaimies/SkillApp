package com.jdevs.timeo.data.source.local

import androidx.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

class Converters {

    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun toOffsetDateTime(value: String): OffsetDateTime =
        formatter.parse(value, OffsetDateTime::from)

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime): String = date.format(formatter)
}
