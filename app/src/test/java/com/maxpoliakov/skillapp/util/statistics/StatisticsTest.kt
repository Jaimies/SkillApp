package com.maxpoliakov.skillapp.util.statistics

import com.maxpoliakov.skillapp.clockOfEpochDay
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.shared.util.setClock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Duration
import java.time.LocalDate

class StatisticsTest : StringSpec({
    val stats = listOf(
        Statistic(LocalDate.ofEpochDay(0), Duration.ofHours(3)),
        Statistic(LocalDate.ofEpochDay(-1), Duration.ofHours(5))
    )

    "getTodayTime() gets today's time" {
        setClock(clockOfEpochDay(0))
        stats.getTodayTime() shouldBe Duration.ofHours(3)
        setClock(clockOfEpochDay(-1))
        stats.getTodayTime() shouldBe Duration.ofHours(5)
    }

    "getTodayTime() returns Duration.ZERO, if there are no records for today" {
        setClock(clockOfEpochDay(-2))
        stats.getTodayTime() shouldBe Duration.ZERO
    }

    afterEach { setClock(Clock.systemDefaultZone()) }
})
