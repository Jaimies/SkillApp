package com.theskillapp.skillapp.data

import com.theskillapp.skillapp.data.db.Converters
import com.theskillapp.skillapp.domain.model.Goal
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalTime

class ConvertersTest : StringSpec({
    val epoch = LocalDate.ofEpochDay(0)

    "fromLocalDate()" {
        Converters.fromLocalDate(epoch) shouldBe "1970-01-01"
    }

    "toLocalDate()" {
        Converters.toLocalDate("1970-01-01") shouldBe epoch
    }

    "fromLocalTime()" {
        Converters.fromLocalTime(LocalTime.NOON) shouldBe "12:00:00"
        Converters.fromLocalTime(null) shouldBe null
    }

    "toLocalTime()" {
        Converters.toLocalTime("12:00:00") shouldBe LocalTime.NOON
        Converters.toLocalTime(null) shouldBe null
    }

    "toZonedDateTime() and fromZonedDateTime()" {
        listOf(
            "2011-12-03T10:15:30+01:00[Europe/Paris]",
            "2045-05-15T17:15:21+09:00[Asia/Tokyo]",
        ).forEach { dateString ->
            Converters.fromZonedDateTime(Converters.toZonedDateTime(dateString)) shouldBe dateString
        }
    }

    "toGoalType() and fromGoalType()" {
        listOf(Goal.Type.Daily, Goal.Type.Weekly).forEach { interval ->
            Converters.toGoalType(Converters.fromGoalType(interval)) shouldBe interval
        }
    }
})
