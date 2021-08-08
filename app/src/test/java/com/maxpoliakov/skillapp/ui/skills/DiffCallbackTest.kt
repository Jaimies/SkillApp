package com.maxpoliakov.skillapp.ui.skills

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.skills.stopwatch.StopwatchUiModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration.ZERO
import java.time.LocalDate


class DiffCallbackTest : StringSpec({
    val callback = SkillDiffCallback()

    "areItemsTheSame" {
        callback.areItemsTheSame(createSkill(1, "B"), createSkill(1, "A")) shouldBe true
        callback.areItemsTheSame(createSkill(1, "A"), createSkill(2, "A")) shouldBe false
        callback.areItemsTheSame(StopwatchUiModel, StopwatchUiModel) shouldBe true
        callback.areItemsTheSame(StopwatchUiModel, createSkill(1, "A")) shouldBe false
    }

    "areContentsTheSame" {
        callback.areContentsTheSame(createSkill(1, "A"), createSkill(1, "A")) shouldBe true
        callback.areContentsTheSame(createSkill(1, "A"), createSkill(1, "B")) shouldBe false
        callback.areContentsTheSame(StopwatchUiModel, StopwatchUiModel) shouldBe true
    }

    "contents are the same if order is order is different" {
        callback.areContentsTheSame(createSkill(0, "A", 2), createSkill(0, "A", 3))
    }
})

private fun createSkill(id: Int, name: String, order: Int = 0) : Skill {
    return Skill(name, ZERO, ZERO, ZERO, id, LocalDate.ofEpochDay(0), order)
}
