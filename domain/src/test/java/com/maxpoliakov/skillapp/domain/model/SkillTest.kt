package com.maxpoliakov.skillapp.domain.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SkillTest : StringSpec({
    fun createSkill(unit: MeasurementUnit<*> = MeasurementUnit.Millis, groupId: Int = 1) = Skill(
        name = "",
        unit = unit,
        totalCount = 10,
        initialCount = 5,
        groupId = groupId,
    )

    "isInAGroup" {
        createSkill(groupId = 1).isInAGroup shouldBe true
        createSkill(groupId = 0).isInAGroup shouldBe true
        createSkill(groupId = 2).isInAGroup shouldBe true
        createSkill(groupId = -1).isInAGroup shouldBe false
    }

    "isNotInAGroup" {
        createSkill(groupId = 1).isNotInAGroup shouldBe false
        createSkill(groupId = 0).isNotInAGroup shouldBe false
        createSkill(groupId = 2).isNotInAGroup shouldBe false
        createSkill(groupId = -1).isNotInAGroup shouldBe true
    }

    "canBeInGroupWith()" {
        createSkill(unit = MeasurementUnit.Meters).canBeInGroupWith(createSkill(unit = MeasurementUnit.Pages)) shouldBe false
        createSkill(unit = MeasurementUnit.Meters).canBeInGroupWith(createSkill(unit = MeasurementUnit.Meters)) shouldBe true
        createSkill(unit = MeasurementUnit.Millis).canBeInGroupWith(createSkill(unit = MeasurementUnit.Meters)) shouldBe false
    }
})
