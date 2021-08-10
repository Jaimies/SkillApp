package com.maxpoliakov.skillapp.ui.skills.group

import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.util.time.toReadableHours

data class SkillGroupState(val name: String, val totalTime: String)

fun SkillGroup.toSkillGroupState() = SkillGroupState(name, totalTime.toReadableHours())
