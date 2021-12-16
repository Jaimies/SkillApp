package com.maxpoliakov.skillapp.ui.skills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Orderable
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.model.StopwatchState.Running
import com.maxpoliakov.skillapp.domain.repository.BillingRepository
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.grouping.AddOrRemoveSkillToGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.SkillsAndGroups
import com.maxpoliakov.skillapp.domain.usecase.skill.UpdateOrderUseCase
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkillsViewModel @Inject constructor(
    getSkills: GetSkillsAndSkillGroupsUseCase,
    private val manageGroup: AddOrRemoveSkillToGroupUseCase,
    private val updateOrder: UpdateOrderUseCase,
    private val stopwatchUtil: StopwatchUtil,
    private val billingRepository: BillingRepository,
    private val ioScope: CoroutineScope,
) : ViewModel() {

    val skillsAndGroups = isSubscribed.flatMapLatest { isSubscribed ->
        if (isSubscribed)
            getSkills.getSkillsAndGroups()
        else
            getSkills.getSkills()
                .map { skills ->
                    SkillsAndGroups(skills.map { skill -> skill.copy(groupId = -1) }, listOf())
                }
    }

    val isSubscribed get() = billingRepository.isSubscribed
    val subscriptionState get() = billingRepository.subscriptionState.asLiveData()

    val isEmpty = skillsAndGroups.map {
        val isEmpty = it.skills.isEmpty() && it.groups.isEmpty()
        if (isEmpty) delay(50)
        isEmpty
    }.asLiveData()

    val isActive = stopwatchUtil.state.map { it is Running }.asLiveData()

    val navigateToAddEdit = SingleLiveEvent<Any>()

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

    fun stopTimer() = ioScope.launch { stopwatchUtil.stop() }
    fun navigateToAddSkill() = navigateToAddEdit.call()
}
