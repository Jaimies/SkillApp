package com.maxpoliakov.skillapp.domain.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.util.Locale

class StatisticIntervalTest : StringSpec({
    "toNumber()" {
        val date = LocalDate.parse("1971-01-01")
        StatisticInterval.Daily.toNumber(date) shouldBe 365L
        StatisticInterval.Weekly.toNumber(date) shouldBe 52
        StatisticInterval.Monthly.toNumber(date) shouldBe 12
        StatisticInterval.Yearly.toNumber(date) shouldBe 1
    }

    "toDate()" {
        StatisticInterval.Daily.toDate(5) shouldBe LocalDate.parse("1970-01-06")
        StatisticInterval.Weekly.toDate(5) shouldBe LocalDate.parse("1970-02-01")
        StatisticInterval.Monthly.toDate(5) shouldBe LocalDate.parse("1970-06-01")
        StatisticInterval.Yearly.toDate(5) shouldBe LocalDate.parse("1975-01-01")
    }

    "getDateRangeContaining()" {
        Locale.setDefault(Locale.UK)

        val date = LocalDate.parse("1970-01-08")
        StatisticInterval.Daily.getDateRangeContaining(date) shouldBe LocalDate.parse("1970-01-08")..LocalDate.parse("1970-01-08")
        StatisticInterval.Weekly.getDateRangeContaining(date) shouldBe LocalDate.parse("1970-01-05")..LocalDate.parse("1970-01-11")
        StatisticInterval.Monthly.getDateRangeContaining(date) shouldBe LocalDate.parse("1970-01-01")..LocalDate.parse("1970-01-31")
        StatisticInterval.Yearly.getDateRangeContaining(date) shouldBe LocalDate.parse("1970-01-01")..LocalDate.parse("1970-12-31")
    }
})
