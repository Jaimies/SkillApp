package com.maxpoliakov.skillapp.data.db

import androidx.room.TypeConverter
import com.maxpoliakov.skillapp.domain.model.Goal
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Converters {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = dateFormatter.parse(value, LocalDate::from)

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.format(dateFormatter)

    @TypeConverter
    fun toTimeRange(value: String?): ClosedRange<LocalTime>? {
        if (value == null) return null

        return runCatching {
            value.split("|")
                .map(this::toLocalDateTime)
                .let { parts -> parts[0]..parts[1] }
        }.getOrNull()
    }

    @TypeConverter
    fun fromTimeRange(range: ClosedRange<LocalTime>?): String? {
        if (range == null) return null

        return listOf(range.start, range.endInclusive)
            .joinToString("|", transform = this::fromLocalDateTime)
    }

    @TypeConverter
    fun toLocalDateTime(value: String): LocalTime = timeFormatter.parse(value, LocalTime::from)

    @TypeConverter
    fun fromLocalDateTime(date: LocalTime): String = date.format(timeFormatter)

    @TypeConverter
    fun toGoalType(value: String): Goal.Type = Goal.Type.valueOf(value)

    @TypeConverter
    fun fromGoalType(type: Goal.Type): String = type.name
}
