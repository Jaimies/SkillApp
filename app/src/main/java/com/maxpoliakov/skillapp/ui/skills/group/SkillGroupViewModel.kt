package com.maxpoliakov.skillapp.ui.skills.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.util.navigation.NavigationUtil
import javax.inject.Inject

class SkillGroupViewModel @Inject constructor(
    private val navigationUtil: NavigationUtil,
) {
    private val _skillGroup = MutableLiveData<SkillGroup>()
    val skillGroup: LiveData<SkillGroup> get() = _skillGroup

    val state = skillGroup.map(SkillGroup::toSkillGroupState)

    fun setSkillGroup(skillGroup: SkillGroup) {
        _skillGroup.value = skillGroup
    }

    fun navigateToDetail() {
        skillGroup.value?.let { group ->
            navigationUtil.navigateToSkillGroupDetail(group.id)
        }
    }
}
