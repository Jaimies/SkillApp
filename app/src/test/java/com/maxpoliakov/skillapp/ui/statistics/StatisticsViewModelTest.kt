package com.maxpoliakov.skillapp.ui.statistics

import com.maxpoliakov.skillapp.clockOfEpochDay
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.shared.util.setClock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDate

class StatisticsViewModelTest : StringSpec({
    beforeSpec { setClock(clockOfEpochDay(0)) }

    "calculateSummary()" {
        val skills = listOf(
            createSkill(Duration.ofHours(2), Duration.ofHours(1), LocalDate.ofEpochDay(-10)),
            createSkill(Duration.ofHours(4), Duration.ofHours(2), LocalDate.ofEpochDay(-5)),
            createSkill(Duration.ofHours(6), Duration.ofHours(3), LocalDate.ofEpochDay(-3))
        )

        val stats = listOf(
            Statistic(LocalDate.ofEpochDay(-2), Duration.ofHours(1)),
            Statistic(LocalDate.ofEpochDay(-1), Duration.ofHours(2)),
            Statistic(LocalDate.ofEpochDay(0), Duration.ofHours(3))
        )

        calculateSummary(skills, stats) shouldBe ProductivitySummary(
            totalTime = Duration.ofHours(12),
            lastWeekTime = Duration.ofHours(6),
            averageWeekTime = Duration.ofHours(3)
        )
    }
})

private fun createSkill(time: Duration, initialTime: Duration, creationDate: LocalDate): Skill {
    return Skill(
        name = "name",
        totalTime = time,
        initialTime = initialTime,
        date = creationDate
    )
}
