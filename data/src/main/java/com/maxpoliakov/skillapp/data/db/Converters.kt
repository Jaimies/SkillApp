package com.maxpoliakov.skillapp.data.db

import androidx.room.TypeConverter
import com.maxpoliakov.skillapp.domain.model.Goal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Converters {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = dateFormatter.parse(value, LocalDate::from)

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.format(dateFormatter)

    @TypeConverter
    fun toDateRange(value: String?): ClosedRange<LocalDateTime>? {
        if (value == null) return null

        return runCatching {
            value.split("|")
                .map(this::toLocalDateTime)
                .let { parts -> parts[0]..parts[1] }
        }.getOrNull()
    }

    // todo consider a better format
    @TypeConverter
    fun fromDateRange(range: ClosedRange<LocalDateTime>?): String? {
        if (range == null) return null

        return listOf(range.start, range.endInclusive)
            .joinToString("|", transform = this::fromLocalDateTime)
    }

    @TypeConverter
    fun toLocalDateTime(value: String): LocalDateTime = dateTimeFormatter.parse(value, LocalDateTime::from)

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime): String = date.format(dateTimeFormatter)

    @TypeConverter
    fun toGoalType(value: String): Goal.Type = Goal.Type.valueOf(value)

    @TypeConverter
    fun fromGoalType(type: Goal.Type): String = type.name
}
