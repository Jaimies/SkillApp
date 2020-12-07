package com.maxpoliakov.skillapp.util.time

import com.maxpoliakov.skillapp.daysAgoDate
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration

class ProgressTest : StringSpec({
    "getAvgWeekTime()" {
        getAvgWeekHours(Duration.ofHours(2), daysAgoDate(6)) shouldBe Duration.ofHours(2)
        getAvgWeekHours(Duration.ofHours(2), daysAgoDate(7)) shouldBe Duration.ofHours(1)
        getAvgWeekHours(Duration.ofHours(3), daysAgoDate(14)) shouldBe Duration.ofHours(1)
    }
})

