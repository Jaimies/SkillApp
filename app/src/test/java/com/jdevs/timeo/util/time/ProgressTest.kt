package com.jdevs.timeo.util.time

import com.jdevs.timeo.daysAgoDate
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ProgressTest : StringSpec({
    "get avg week hours" {
        getAvgWeekHours(238, daysAgoDate(7)) shouldBe "2"
        getAvgWeekHours(444, daysAgoDate(8)) shouldBe "3.7"
    }

    "get days spent since" {
        daysAgoDate(1).getDaysSpentSince() shouldBe 2L
        daysAgoDate(7).getDaysSpentSince() shouldBe 8L
        daysAgoDate(0).getDaysSpentSince() shouldBe 1L
        daysAgoDate(-1).getDaysSpentSince() shouldBe 1L
    }
})
