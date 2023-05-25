package com.maxpoliakov.skillapp.data.db

import androidx.room.TypeConverter
import com.maxpoliakov.skillapp.domain.model.Goal
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object Converters {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME
    private val zonedDateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = dateFormatter.parse(value, LocalDate::from)

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.format(dateFormatter)

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? {
        if (value == null) return null
        return timeFormatter.parse(value, LocalTime::from)
    }

    @TypeConverter
    fun fromLocalTime(date: LocalTime?): String? = date?.format(timeFormatter)

    @TypeConverter
    fun toGoalType(value: String): Goal.Type = Goal.Type.valueOf(value)

    @TypeConverter
    fun fromGoalType(type: Goal.Type): String = type.name

    @TypeConverter
    fun fromZonedDateTime(dateTime: ZonedDateTime): String = zonedDateTimeFormatter.format(dateTime)

    @TypeConverter
    fun toZonedDateTime(value: String): ZonedDateTime = zonedDateTimeFormatter.parse(value, ZonedDateTime::from)
}
