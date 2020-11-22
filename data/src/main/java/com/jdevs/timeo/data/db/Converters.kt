package com.jdevs.timeo.data.db

import androidx.room.TypeConverter
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun toOffsetDateTime(value: String): OffsetDateTime =
        formatter.parse(value, OffsetDateTime::from)

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime): String = date.format(formatter)

    @TypeConverter
    fun toDuration(value: Long): Duration = Duration.ofMinutes(value)

    @TypeConverter
    fun fromDuration(duration: Duration): Long = duration.toMinutes()
}
