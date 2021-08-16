package com.maxpoliakov.skillapp.ui.skills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.grouping.AddOrRemoveSkillToGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.UpdateOrderUseCase
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkillsViewModel @Inject constructor(
    getSkills: GetSkillsAndSkillGroupsUseCase,
    private val manageGroup: AddOrRemoveSkillToGroupUseCase,
    private val updateOrder: UpdateOrderUseCase,
    private val stopwatchUtil: StopwatchUtil
) : ViewModel() {

    val skillsAndGroups = getSkills.getSkillsAndGroups()

    val isEmpty = skillsAndGroups.map { it.skills.isEmpty() && it.groups.isEmpty() }.asLiveData()

    val isActive = stopwatchUtil.state.map { it is Running }.asLiveData()

    val navigateToAddEdit = SingleLiveEvent<Any>()

    fun updateOrder(skills: List<Skill>) = viewModelScope.launch {
        updateOrder.run(skills)
    }

    fun createGroup(name: String, skills: List<Skill>) = viewModelScope.launch {
        manageGroup.createGroup(name, skills)
    }

    fun addToGroup(skillId: Int, groupId: Int) = viewModelScope.launch {
        manageGroup.addToGroup(skillId, groupId)
    }

    fun removeFromGroup(skillId: Int) = viewModelScope.launch {
        manageGroup.removeFromGroup(skillId)
    }

    fun stopTimer() = stopwatchUtil.stop()
    fun navigateToAddSkill() = navigateToAddEdit.call()
}
