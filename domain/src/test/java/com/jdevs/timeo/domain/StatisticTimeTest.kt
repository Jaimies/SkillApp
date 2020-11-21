package com.jdevs.timeo.domain

import com.jdevs.timeo.domain.model.StatisticTime
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration

class StatisticTimeTest : StringSpec({

    val timeMap = mapOf(
        0 to Duration.ofHours(10),
        1 to Duration.ofHours(20)
    )

    val time = StatisticTime(timeMap)

    "getActivityTime" {
        time.getActivityTime(0) shouldBe Duration.ofHours(10)
        time.getActivityTime(1) shouldBe Duration.ofHours(20)
        time.getActivityTime(5) shouldBe Duration.ZERO
    }

    "getTotalTime" {
        time.getTotalTime() shouldBe Duration.ofHours(30)
    }
})
