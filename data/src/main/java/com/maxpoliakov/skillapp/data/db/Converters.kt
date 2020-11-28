package com.maxpoliakov.skillapp.data.db

import androidx.room.TypeConverter
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    @TypeConverter
    fun toLocalDateTime(value: String): LocalDateTime =
        formatter.parse(value, LocalDateTime::from)

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime): String = date.format(formatter)

    @TypeConverter
    fun toDuration(value: Long): Duration = Duration.ofMinutes(value)

    @TypeConverter
    fun fromDuration(duration: Duration): Long = duration.toMinutes()
}
