package com.maxpoliakov.skillapp.ui.skills

import com.maxpoliakov.skillapp.domain.model.Skill
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
})

private fun createSkill(id: Int, name: String) : Skill {
    return Skill(name, ZERO, ZERO, ZERO, id, LocalDate.ofEpochDay(0))
}
