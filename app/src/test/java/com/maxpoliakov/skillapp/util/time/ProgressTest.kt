package com.maxpoliakov.skillapp.util.time

import com.maxpoliakov.skillapp.daysAgoDate
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration

class ProgressTest : StringSpec({
    "get avg week hours" {
        getAvgWeekHours(Duration.ofMinutes(238), daysAgoDate(7)) shouldBe "2"
        getAvgWeekHours(Duration.ofMinutes(444), daysAgoDate(8)) shouldBe "3.7"
    }
})
