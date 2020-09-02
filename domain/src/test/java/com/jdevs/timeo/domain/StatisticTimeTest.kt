package com.jdevs.timeo.domain

import com.jdevs.timeo.domain.model.StatisticTime
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class StatisticTimeTest : StringSpec({

    val timeMap = mapOf(
        0 to 10,
        1 to 20
    )

    val time = StatisticTime(timeMap)

    "getActivityTime" {
        time.getActivityTime(0) shouldBe 10
        time.getActivityTime(1) shouldBe 20
        time.getActivityTime(5) shouldBe 0
    }

    "getTotalTime" {
        time.getTotalTime() shouldBe 30
    }
})