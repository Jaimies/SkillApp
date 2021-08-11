package com.maxpoliakov.skillapp.ui.skills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.usecase.grouping.CreateGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.AddSkillToGroupUseCase
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
    private val createGroup: CreateGroupUseCase,
    private val addSkillToGroup: AddSkillToGroupUseCase,
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
        createGroup.run(name, skills)
    }

    fun addToGroup(skill: Skill, group: SkillGroup) = viewModelScope.launch {
        addSkillToGroup.run(skill.id, group.id)
    }

    fun stopTimer() = stopwatchUtil.stop()
    fun navigateToAddSkill() = navigateToAddEdit.call()
}
