package com.maxpoliakov.skillapp.ui.skills.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.SkillGroup

class SkillGroupViewModel {
    private val _skillGroup = MutableLiveData<SkillGroup>()
    val skillGroup: LiveData<SkillGroup> get() = _skillGroup

    val state = skillGroup.map(SkillGroup::toSkillGroupState)

    fun setSkillGroup(skillGroup: SkillGroup) {
        _skillGroup.value = skillGroup
    }
}
