package com.maxpoliakov.skillapp.data.db

import androidx.room.TypeConverter
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = formatter.parse(value, LocalDate::from)

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.format(formatter)

    @TypeConverter
    fun toDuration(value: Long): Duration = Duration.ofMinutes(value)

    @TypeConverter
    fun fromDuration(duration: Duration): Long = duration.toMinutes()
}
