package com.maxpoliakov.skillapp.ui.skills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Orderable
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Running
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.grouping.AddOrRemoveSkillToGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.UpdateOrderUseCase
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkillsViewModel @Inject constructor(
    getSkills: GetSkillsAndSkillGroupsUseCase,
    private val manageGroup: AddOrRemoveSkillToGroupUseCase,
    private val updateOrder: UpdateOrderUseCase,
    private val editingModeManager: EditingModeManager,
    stopwatchUtil: StopwatchUtil,
) : ViewModel() {

    val skillsAndGroups = getSkills.getSkillsAndGroups()

    val isEmptyFlow = skillsAndGroups.map {
        val isEmpty = it.skills.isEmpty() && it.groups.isEmpty()
        if (isEmpty) delay(50)
        isEmpty
    }.distinctUntilChanged()

    val isEmpty = isEmptyFlow.asLiveData()

    val isActive = stopwatchUtil.state.map { it is Running }.asLiveData()

    val navigateToAddSkill = SingleLiveEvent<Any>()

    val isInEditingMode get() = editingModeManager.isInEditingMode.value

    fun updateOrder(skills: List<Orderable>) = viewModelScope.launch {
        updateOrder.run(skills)
    }

    fun createGroupAsync(originalSkill: Skill, group: SkillGroup) = viewModelScope.async {
        logEvent("create_group")
        manageGroup.createGroup(originalSkill, group)
    }

    fun addToGroup(skill: Skill, groupId: Int) = viewModelScope.launch {
        manageGroup.addToGroup(skill, groupId)
    }

    fun removeFromGroup(skill: Skill) = viewModelScope.launch {
        manageGroup.removeFromGroup(skill)
    }

    fun toggleEditingMode() = editingModeManager.toggleEditingMode()
    fun navigateToAddSkill() = navigateToAddSkill.call()
}
