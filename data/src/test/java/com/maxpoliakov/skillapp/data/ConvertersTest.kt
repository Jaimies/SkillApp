package com.maxpoliakov.skillapp.data

import com.maxpoliakov.skillapp.data.db.Converters
import com.maxpoliakov.skillapp.domain.model.Goal
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalTime

class ConvertersTest : StringSpec({
    val epoch = LocalDate.ofEpochDay(0)

    "fromTimeRange()" {
        Converters.fromTimeRange(
            LocalTime.parse("05:20:30")..LocalTime.parse("20:30:00"),
        ) shouldBe "05:20:30|20:30:00"

        Converters.fromTimeRange(
            LocalTime.parse("13:15:15")..LocalTime.parse("02:40:55"),
        ) shouldBe "13:15:15|02:40:55"
    }

    "toTimeRange()" {
        Converters.toTimeRange(
            "05:20:30|20:30:00",
        ) shouldBe LocalTime.parse("05:20:30")..LocalTime.parse("20:30:00")

        Converters.toTimeRange(
            "13:15:15|02:40:55",
        ) shouldBe LocalTime.parse("13:15:15")..LocalTime.parse("02:40:55")
    }

    "toDateRange() returns null if value cannot be parsed" {
        Converters.toTimeRange("invalid format") shouldBe null
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
