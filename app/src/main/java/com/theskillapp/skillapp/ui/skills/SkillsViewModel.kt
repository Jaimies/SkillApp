package com.theskillapp.skillapp.ui.skills

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.theskillapp.skillapp.domain.di.ApplicationScope
import com.theskillapp.skillapp.domain.model.Orderable
import com.theskillapp.skillapp.domain.model.Record
import com.theskillapp.skillapp.domain.model.Skill
import com.theskillapp.skillapp.domain.model.SkillGroup
import com.theskillapp.skillapp.domain.model.Timer
import com.theskillapp.skillapp.domain.model.Trackable
import com.theskillapp.skillapp.domain.stopwatch.Stopwatch
import com.theskillapp.skillapp.domain.usecase.grouping.AddOrRemoveSkillToGroupUseCase
import com.theskillapp.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.theskillapp.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.theskillapp.skillapp.domain.usecase.skill.UpdateOrderUseCase
import com.theskillapp.skillapp.shared.lifecycle.SingleLiveEvent
import com.theskillapp.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import com.theskillapp.skillapp.ui.skills.recyclerview.SkillListAdapter.Companion.getStopwatchItemId
import com.theskillapp.skillapp.ui.skills.recyclerview.group.footer.SkillGroupFooter
import com.theskillapp.skillapp.ui.skills.recyclerview.stopwatch.StopwatchUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkillsViewModel @Inject constructor(
    getSkills: GetSkillsAndSkillGroupsUseCase,
    private val getSkill: GetSkillByIdUseCase,
    private val manageGroup: AddOrRemoveSkillToGroupUseCase,
    private val updateOrder: UpdateOrderUseCase,
    private val editingModeManager: EditingModeManager,
    @ApplicationScope
    private val scope: CoroutineScope,
    private val stopwatch: Stopwatch,
) : ViewModel() {

    private val stopwatches = stopwatch.state.flatMapLatest { state -> state.timers.mapToUI() }

    val list = getSkills.getSkillsAndGroups().combine(stopwatches) { skillsAndGroups, stopwatches ->
        val list = (skillsAndGroups.skills + skillsAndGroups.groups)
            .sortedBy(Orderable::order)
            .flatMap(this::getListItems)

        stopwatches + list
    }

    private fun List<Timer>.mapToUI(): Flow<List<StopwatchUiModel>> {
        if (this.isEmpty()) return flowOf(listOf())
        return combine(this.map { it.mapToUI() }) { it.toList() }
    }

    private fun Timer.mapToUI(): Flow<StopwatchUiModel> {
        return getSkill.run(skillId).map { skill -> this.mapToUI(skill) }
    }

    private fun Timer.mapToUI(skill: Skill) = StopwatchUiModel(
        skill,
        startTime,
        { stop() },
        { navigateToDetailFromTimer(skill) },
    )

    private fun Timer.stop() = scope.launch {
        val change = stopwatch.stop(skillId)
        _showRecordsAdded.value = change.addedRecords
    }

    private fun navigateToDetailFromTimer(skill: Skill) {
        _navigateToSkillDetail.value = skill to getStopwatchItemId(skill.id)
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

    val isEmptyFlow = list.map(List<Any>::isEmpty).distinctUntilChanged()
    val isEmpty = isEmptyFlow.asLiveData()

    val navigateToAddSkill = SingleLiveEventWithoutData()

    private val _navigateToSkillDetail = SingleLiveEvent<Pair<Skill, Long>>()
    val navigateToSkillDetail: LiveData<Pair<Skill, Long>> get() = _navigateToSkillDetail

    private val _showRecordsAdded = SingleLiveEvent<List<Record>>()
    val showRecordsAdded: LiveData<List<Record>> get() = _showRecordsAdded

    val isInEditingMode get() = editingModeManager.isInEditingMode.value

    fun updateOrder(skills: List<Orderable>) = viewModelScope.launch {
        updateOrder.run(skills)
    }

    fun createGroupAsync(originalSkill: Skill, group: SkillGroup) = viewModelScope.async {
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
