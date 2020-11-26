package com.maxpoliakov.skillapp.shared.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.OffsetDateTime

class TimeTest : StringSpec({
    "shortName" {
        DayOfWeek.MONDAY.shortName shouldBe "Mon"
        DayOfWeek.TUESDAY.shortName shouldBe "Tue"
    }

    "daysAgo" {
        LocalDate.now().minusDays(1).daysAgo shouldBe 1
        OffsetDateTime.now().minusDays(2).daysAgo shouldBe 2
    }

    "daysSinceEpoch" {
        LocalDate.ofEpochDay(0).daysSinceEpoch shouldBe 0
        LocalDate.ofEpochDay(1).daysSinceEpoch shouldBe 1
        LocalDate.ofEpochDay(365).daysSinceEpoch shouldBe 365
    }
})
