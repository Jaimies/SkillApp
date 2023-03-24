package com.maxpoliakov.skillapp.domain.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.util.Locale

class StatisticIntervalTest : StringSpec({
    beforeSpec {
        Locale.setDefault(Locale.UK)
    }

    "toNumber()" {
        val date = LocalDate.parse("1971-01-01")
        StatisticInterval.Daily.toNumber(date) shouldBe 365L
        StatisticInterval.Weekly.toNumber(date) shouldBe 52
        StatisticInterval.Monthly.toNumber(date) shouldBe 12
        StatisticInterval.Yearly.toNumber(date) shouldBe 1
    }

    "toDateRange()" {
        StatisticInterval.Daily.toDateRange(5) shouldBe LocalDate.parse("1970-01-06")..LocalDate.parse("1970-01-06")
        StatisticInterval.Weekly.toDateRange(5) shouldBe LocalDate.parse("1970-02-02")..LocalDate.parse("1970-02-08")
        StatisticInterval.Monthly.toDateRange(5) shouldBe LocalDate.parse("1970-06-01")..LocalDate.parse("1970-06-30")
        StatisticInterval.Yearly.toDateRange(5) shouldBe LocalDate.parse("1975-01-01")..LocalDate.parse("1975-12-31")
    }

    "getDateRangeContaining()" {
        StatisticInterval.Weekly.getDateRangeContaining(LocalDate.parse("1970-02-01")) shouldBe LocalDate.parse("1970-01-26")..LocalDate.parse("1970-02-01")
        StatisticInterval.Weekly.getDateRangeContaining(LocalDate.parse("1970-02-02")) shouldBe LocalDate.parse("1970-02-02")..LocalDate.parse("1970-02-08")
        StatisticInterval.Weekly.getDateRangeContaining(LocalDate.parse("1970-02-03")) shouldBe LocalDate.parse("1970-02-02")..LocalDate.parse("1970-02-08")
        StatisticInterval.Weekly.getDateRangeContaining(LocalDate.parse("1970-02-04")) shouldBe LocalDate.parse("1970-02-02")..LocalDate.parse("1970-02-08")
        StatisticInterval.Weekly.getDateRangeContaining(LocalDate.parse("1970-02-05")) shouldBe LocalDate.parse("1970-02-02")..LocalDate.parse("1970-02-08")
        StatisticInterval.Weekly.getDateRangeContaining(LocalDate.parse("1970-02-06")) shouldBe LocalDate.parse("1970-02-02")..LocalDate.parse("1970-02-08")
        StatisticInterval.Weekly.getDateRangeContaining(LocalDate.parse("1970-02-07")) shouldBe LocalDate.parse("1970-02-02")..LocalDate.parse("1970-02-08")
        StatisticInterval.Weekly.getDateRangeContaining(LocalDate.parse("1970-02-08")) shouldBe LocalDate.parse("1970-02-02")..LocalDate.parse("1970-02-08")
        StatisticInterval.Weekly.getDateRangeContaining(LocalDate.parse("1970-02-09")) shouldBe LocalDate.parse("1970-02-09")..LocalDate.parse("1970-02-15")

        StatisticInterval.Monthly.getDateRangeContaining(LocalDate.parse("1970-01-31")) shouldBe LocalDate.parse("1970-01-01")..LocalDate.parse("1970-01-31")
        StatisticInterval.Monthly.getDateRangeContaining(LocalDate.parse("1970-02-01")) shouldBe LocalDate.parse("1970-02-01")..LocalDate.parse("1970-02-28")
        StatisticInterval.Monthly.getDateRangeContaining(LocalDate.parse("1970-02-28")) shouldBe LocalDate.parse("1970-02-01")..LocalDate.parse("1970-02-28")
        StatisticInterval.Monthly.getDateRangeContaining(LocalDate.parse("1970-03-01")) shouldBe LocalDate.parse("1970-03-01")..LocalDate.parse("1970-03-31")
    }
})
