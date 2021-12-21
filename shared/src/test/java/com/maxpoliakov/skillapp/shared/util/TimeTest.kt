package com.maxpoliakov.skillapp.shared.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.Locale

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

    "atStartOfWeek()" {
        Locale.setDefault(Locale.US)
        LocalDate.ofEpochDay(0).atStartOfWeek() shouldBe LocalDate.parse("1969-12-28")
        LocalDate.ofEpochDay(4).atStartOfWeek() shouldBe LocalDate.parse("1970-01-04")

        Locale.setDefault(Locale.UK)
        LocalDate.ofEpochDay(0).atStartOfWeek() shouldBe LocalDate.parse("1969-12-29")
        LocalDate.ofEpochDay(4).atStartOfWeek() shouldBe LocalDate.parse("1970-01-05")
    }

    "weeksSinceEpoch" {
        LocalDate.ofEpochDay(0).atStartOfWeek().weeksSinceEpoch shouldBe 0
        LocalDate.ofEpochDay(0).atStartOfWeek().plusWeeks(1).weeksSinceEpoch shouldBe 1
        LocalDate.ofEpochDay(0).atStartOfWeek().plusWeeks(250).weeksSinceEpoch shouldBe 250
    }

    "monthsSinceEpoch" {
        LocalDate.ofEpochDay(0).monthsSinceEpoch shouldBe 0
        LocalDate.ofEpochDay(5).plusMonths(1).monthsSinceEpoch shouldBe 1
        LocalDate.ofEpochDay(12).plusMonths(50).monthsSinceEpoch shouldBe 50
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

    "String.toZonedDateTimeOrNull()" {
        val dateString = "2007-12-03T10:15:30+01:00[Europe/Paris]"
        "".toZonedDateTimeOrNull() shouldBe null
        dateString.toZonedDateTimeOrNull() shouldBe ZonedDateTime.parse(dateString)
    }

    "until()" {
        Instant.ofEpochSecond(0).until(Instant.ofEpochSecond(1)) shouldBe Duration.ofSeconds(1)
        Instant.ofEpochSecond(0).until(Instant.ofEpochSecond(2)) shouldBe Duration.ofSeconds(2)
        Instant.ofEpochMilli(0).until(Instant.ofEpochMilli(5)) shouldBe Duration.ofMillis(5)
    }
})
