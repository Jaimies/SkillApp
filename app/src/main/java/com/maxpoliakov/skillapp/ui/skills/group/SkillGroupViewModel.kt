package com.maxpoliakov.skillapp.ui.skills.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent

class SkillGroupViewModel {
    private val _skillGroup = MutableLiveData<SkillGroup>()
    val skillGroup: LiveData<SkillGroup> get() = _skillGroup

    private val _navigateToDetail = SingleLiveEvent<SkillGroup>()
    val navigateToDetail: LiveData<SkillGroup> get() = _navigateToDetail

    val uiUnit = skillGroup.map { group ->
        UiMeasurementUnit.from(group.unit)
    }

    fun setSkillGroup(skillGroup: SkillGroup) {
        _skillGroup.value = skillGroup
    }

    fun navigateToDetail() {
        skillGroup.value?.let { group ->
            _navigateToDetail.value = group
        }
    }
}
