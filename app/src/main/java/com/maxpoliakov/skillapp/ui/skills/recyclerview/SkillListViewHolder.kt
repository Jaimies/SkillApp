package com.maxpoliakov.skillapp.ui.skills.recyclerview

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.ui.common.recyclerview.itemdecoration.fakecardview.PartOfFakeCardView

interface SkillListViewHolder: PartOfFakeCardView {
    val groupId: Int
    val unit: MeasurementUnit<*>

    override val cardId get() = groupId
}
