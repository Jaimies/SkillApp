package com.maxpoliakov.skillapp.ui.skills

import android.view.View
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder

abstract class SkillListViewHolder(view: View) : BaseViewHolder(view) {
    abstract val groupId: Int
    abstract val unit: MeasurementUnit
}
