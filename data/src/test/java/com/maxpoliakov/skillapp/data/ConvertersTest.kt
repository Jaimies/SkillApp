package com.maxpoliakov.skillapp.data

import com.maxpoliakov.skillapp.data.db.Converters
import com.maxpoliakov.skillapp.domain.model.Goal
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class ConvertersTest : StringSpec({
    val epoch = LocalDate.ofEpochDay(0)

    "fromDateRange()" {
        Converters.fromDateRange(
            LocalDateTime.parse("2015-02-15T05:20:30")..LocalDateTime.parse("2020-05-25T20:30:00"),
        ) shouldBe "2015-02-15T05:20:30|2020-05-25T20:30:00"

        Converters.fromDateRange(
            LocalDateTime.parse("2022-11-01T13:15:15")..LocalDateTime.parse("2022-11-15T02:40:55"),
        ) shouldBe "2022-11-01T13:15:15|2022-11-15T02:40:55"
    }

    "toDateRange()" {
        Converters.toDateRange(
            "2015-02-15T05:20:30|2020-05-25T20:30:00",
        ) shouldBe LocalDateTime.parse("2015-02-15T05:20:30")..LocalDateTime.parse("2020-05-25T20:30:00")

        Converters.toDateRange(
            "2022-11-01T13:15:15|2022-11-15T02:40:55",
        ) shouldBe LocalDateTime.parse("2022-11-01T13:15:15")..LocalDateTime.parse("2022-11-15T02:40:55")
    }

    "toDateRange() returns null if value cannot be parsed" {
        Converters.toDateRange("invalid format") shouldBe null
    }

    "fromLocalDate()" {
        Converters.fromLocalDate(epoch) shouldBe "1970-01-01"
    }

    "toLocalDate()" {
        Converters.toLocalDate("1970-01-01") shouldBe epoch
    }

    "toGoalType() and fromGoalType()" {
        listOf(Goal.Type.Daily, Goal.Type.Weekly).forEach { interval ->
            Converters.toGoalType(Converters.fromGoalType(interval)) shouldBe interval
        }
    }
})
