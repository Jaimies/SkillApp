package com.theskillapp.skillapp.ui.skills.recyclerview

import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.domain.model.Skill
import com.theskillapp.skillapp.domain.model.SkillGroup
import com.theskillapp.skillapp.ui.skills.recyclerview.group.footer.SkillGroupFooter
import com.theskillapp.skillapp.ui.skills.recyclerview.stopwatch.StopwatchUiModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class DiffCallbackTest : StringSpec({
    val callback = SkillDiffCallback()

    "areItemsTheSame() with the same type of item" {
        callback.areItemsTheSame(createSkill(1, "B"), createSkill(1, "A")) shouldBe true
        callback.areItemsTheSame(createSkill(1, "A"), createSkill(2, "A")) shouldBe false
        callback.areItemsTheSame(createGroup(1, "A"), createGroup(1, "B")) shouldBe true
        callback.areItemsTheSame(createGroup(2, "A"), createGroup(1, "A")) shouldBe false
        callback.areItemsTheSame(SkillGroupFooter(createGroup(1, "A")), SkillGroupFooter(createGroup(2, "A"))) shouldBe false
        callback.areItemsTheSame(SkillGroupFooter(createGroup(1, "A")), SkillGroupFooter(createGroup(1, "B"))) shouldBe true
    }

    // with the new skill list ui model these tests will become unnecessary
//    "areItemsTheSame() always returns false if the types of items are different" {
//        callback.areItemsTheSame(StopwatchUiModel, StopwatchUiModel) shouldBe true
//        callback.areItemsTheSame(StopwatchUiModel, createSkill(1, "A")) shouldBe false
//        callback.areItemsTheSame(StopwatchUiModel, createGroup(1, "A")) shouldBe false
//        callback.areItemsTheSame(createSkill(1, "A"), createGroup(1, "A")) shouldBe false
//        callback.areItemsTheSame(StopwatchUiModel, createSkill(1, "A")) shouldBe false
//        callback.areItemsTheSame(StopwatchUiModel, createGroup(1, "A")) shouldBe false
//        callback.areItemsTheSame(createSkill(1, "A"), createGroup(1, "A")) shouldBe false
//    }

//    "areContentsTheSame" {
//        callback.areContentsTheSame(createSkill(1, "A"), createSkill(1, "A")) shouldBe true
//        callback.areContentsTheSame(createSkill(1, "A"), createSkill(1, "B")) shouldBe false
//        callback.areContentsTheSame(createGroup(1, "A"), createGroup(1, "A")) shouldBe true
//        callback.areContentsTheSame(createGroup(1, "A"), createGroup(1, "B")) shouldBe false
//        callback.areContentsTheSame(StopwatchUiModel, StopwatchUiModel) shouldBe true
//        callback.areContentsTheSame(SkillGroupFooter(createGroup(1, "A")), SkillGroupFooter(createGroup(1, "A"))) shouldBe true
//    }

    "contents are the same if order is different" {
        callback.areContentsTheSame(createSkill(0, "A", 2), createSkill(0, "A", 3))
    }
})

private fun createSkill(id: Int, name: String, order: Int = 0): Skill {
    return Skill(name, MeasurementUnit.Millis, 0, 0, id, LocalDate.ofEpochDay(0), order)
}

private fun createGroup(id: Int, name: String, order: Int = 0): SkillGroup {
    return SkillGroup(id, name, listOf(), MeasurementUnit.Millis, null, order)
}
