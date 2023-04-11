package com.maxpoliakov.skillapp.ui.skills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Orderable
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.model.Trackable
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch.State.Running
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import com.maxpoliakov.skillapp.domain.usecase.grouping.AddOrRemoveSkillToGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.UpdateOrderUseCase
import com.maxpoliakov.skillapp.shared.analytics.logEvent
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import com.maxpoliakov.skillapp.ui.skills.recyclerview.group.footer.SkillGroupFooter
import com.maxpoliakov.skillapp.ui.skills.recyclerview.stopwatch.StopwatchUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
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
    stopwatch: Stopwatch,
) : ViewModel() {

    val list = getSkills.getSkillsAndGroups().combine(stopwatch.state) { skillsAndGroups, stopwatchState ->
        val list = (skillsAndGroups.skills + skillsAndGroups.groups)
            .sortedBy(Orderable::order)
            .flatMap(this::getListItems)

        if (stopwatchState is Running) listOf(StopwatchUiModel) + list
        else list
    }

    private fun getListItems(item: Trackable): List<Any> {
        return when (item) {
            is Skill -> getListItemsForSkill(item)
            is SkillGroup -> getListItemsForGroup(item)
            else -> listOf()
        }
    }

    private fun getListItemsForSkill(skill: Skill): List<Any> {
        return listOf(skill)
    }

    private fun getListItemsForGroup(group: SkillGroup): List<Any> {
        return listOf(group) + group.skills.sortedBy(Skill::order) + listOf(SkillGroupFooter(group))
    }

    val isEmptyFlow = list.map { list ->
        val isEmpty = list.isEmpty()
        if (isEmpty) delay(50)
        isEmpty
    }.distinctUntilChanged()

    val isEmpty = isEmptyFlow.asLiveData()

    val navigateToAddSkill = SingleLiveEventWithoutData()

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
