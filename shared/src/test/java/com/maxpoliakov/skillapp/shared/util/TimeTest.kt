package com.maxpoliakov.skillapp.shared.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate

class TimeTest : StringSpec({
    "shortName" {
        DayOfWeek.MONDAY.shortName shouldBe "Mon"
        DayOfWeek.TUESDAY.shortName shouldBe "Tue"
    }

    "daysAgo" {
        LocalDate.now().minusDays(1).daysAgo shouldBe 1
        LocalDate.now().minusDays(2).daysAgo shouldBe 2
    }

    "daysSinceEpoch" {
        LocalDate.ofEpochDay(0).daysSinceEpoch shouldBe 0
        LocalDate.ofEpochDay(1).daysSinceEpoch shouldBe 1
        LocalDate.ofEpochDay(365).daysSinceEpoch shouldBe 365
    }

    "millisSinceEpoch" {
        LocalDate.ofEpochDay(0).millisSinceEpoch shouldBe 0
        LocalDate.ofEpochDay(1).millisSinceEpoch shouldBe 86_400_000
        LocalDate.ofEpochDay(10).millisSinceEpoch shouldBe 864_000_000
    }

    "dateOfEpochMillis()" {
        dateOfEpochMillis(0) shouldBe LocalDate.ofEpochDay(0)
        dateOfEpochMillis(86_400_000) shouldBe LocalDate.ofEpochDay(1)
        dateOfEpochMillis(86_399_999) shouldBe LocalDate.ofEpochDay(0)
    }

    "durationOfHoursAndMinutes()" {
        durationOfHoursAndMinutes(0, 0) shouldBe Duration.ZERO
        durationOfHoursAndMinutes(0, 1) shouldBe Duration.ofMinutes(1)
        durationOfHoursAndMinutes(1, 0) shouldBe Duration.ofHours(1)
    }
})
