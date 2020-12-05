package com.maxpoliakov.skillapp.ui.skills

import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.ui.common.recordable.RecordableState
import com.maxpoliakov.skillapp.ui.common.recordable.RecordableViewModel

class SkillViewModel : RecordableViewModel<RecordableState, SkillItem>() {
    override fun createState(item: SkillItem) = RecordableState(item)
}
