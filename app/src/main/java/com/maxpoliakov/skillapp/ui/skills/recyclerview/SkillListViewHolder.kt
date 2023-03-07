package com.maxpoliakov.skillapp.ui.skills.recyclerview

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

interface SkillListViewHolder {
    val groupId: Int
    val unit: MeasurementUnit<*>
}
