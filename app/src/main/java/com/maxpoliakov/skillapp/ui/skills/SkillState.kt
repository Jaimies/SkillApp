package com.maxpoliakov.skillapp.ui.skills

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.util.time.toReadableHours

data class SkillState(val name: String, val time: String)

fun Skill.toSkillState() = SkillState(name, totalTime.toReadableHours())
