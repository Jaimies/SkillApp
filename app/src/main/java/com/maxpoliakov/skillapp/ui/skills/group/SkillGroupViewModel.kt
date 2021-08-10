package com.maxpoliakov.skillapp.ui.skills.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxpoliakov.skillapp.domain.model.SkillGroup

class SkillGroupViewModel {
    private val _skillGroup = MutableLiveData<SkillGroupState>()
    val skillGroup: LiveData<SkillGroupState> get() = _skillGroup

    fun setSkillGroup(skillGroup: SkillGroup) {
        _skillGroup.value = skillGroup.toSkillGroupState()
    }
}
