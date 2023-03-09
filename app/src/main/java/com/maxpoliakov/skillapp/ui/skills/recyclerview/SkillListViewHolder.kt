package com.maxpoliakov.skillapp.ui.skills.recyclerview

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.ui.common.cardviewdecoration.PartOfFakeCardView

interface SkillListViewHolder: PartOfFakeCardView {
    val groupId: Int
    val unit: MeasurementUnit<*>

    override val cardId get() = groupId
}
