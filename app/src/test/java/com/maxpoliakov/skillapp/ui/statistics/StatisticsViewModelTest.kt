package com.maxpoliakov.skillapp.ui.statistics

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.test.clockOfEpochDay
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.shared.util.setClock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDate

class StatisticsViewModelTest : StringSpec({
    beforeSpec { setClock(clockOfEpochDay(0)) }

    "calculateSummary()" {
        val skills = listOf(
            createSkill(Duration.ofHours(5), Duration.ofHours(1)),
            createSkill(Duration.ofHours(6), Duration.ofHours(2)),
            createSkill(Duration.ofHours(11), Duration.ofHours(3))
        )

        calculateSummary(skills) shouldBe ProductivitySummary(
            totalCount = Duration.ofHours(22).toMillis(),
            lastWeekCount = Duration.ofHours(6).toMillis(),
            unit = UiMeasurementUnit.Millis,
        )
    }
})

private fun createSkill(time: Duration, lastWeekTime: Duration): Skill {
    return Skill(
        name = "name",
        totalCount = time.toMillis(),
        initialCount = time.toMillis() / 2,
        lastWeekCount = lastWeekTime.toMillis(),
        date = LocalDate.ofEpochDay(0),
        unit = MeasurementUnit.Millis,
    )
}
