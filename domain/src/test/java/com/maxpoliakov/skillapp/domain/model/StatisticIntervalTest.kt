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

    "toDateRange()" {
        StatisticInterval.Daily.toDateRange(5) shouldBe LocalDate.parse("1970-01-06")..LocalDate.parse("1970-01-06")
        StatisticInterval.Weekly.toDateRange(5) shouldBe LocalDate.parse("1970-02-01")..LocalDate.parse("1970-02-07")
        StatisticInterval.Monthly.toDateRange(5) shouldBe LocalDate.parse("1970-06-01")..LocalDate.parse("1970-06-30")
        StatisticInterval.Yearly.toDateRange(5) shouldBe LocalDate.parse("1975-01-01")..LocalDate.parse("1975-12-31")
    }
})
