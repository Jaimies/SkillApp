package com.theskillapp.skillapp.ui.skillgroup

import androidx.lifecycle.asLiveData
import com.theskillapp.skillapp.domain.model.Skill
import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria
import com.theskillapp.skillapp.domain.model.Timer
import com.theskillapp.skillapp.domain.stopwatch.Stopwatch
import com.theskillapp.skillapp.domain.usecase.grouping.GetGroupUseCase
import com.theskillapp.skillapp.domain.usecase.grouping.UpdateGroupUseCase
import com.theskillapp.skillapp.model.ProductivitySummary
import com.theskillapp.skillapp.model.mapToDomain
import com.theskillapp.skillapp.shared.DetailsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class SkillGroupViewModel @Inject constructor(
    args: SkillGroupFragmentArgs,
    getGroup: GetGroupUseCase,
    stopwatch: Stopwatch,
    private val updateGroup: UpdateGroupUseCase,
) : DetailsViewModel(
    stopwatch,
    getGroup.getById(args.groupId),
) {
    private val groupId = args.groupId

    private val _group = getGroup.getById(groupId)
    val group = _group.asLiveData()

    override val selectionCriteria = SkillSelectionCriteria.InGroupWithId(groupId)

    val summary by lazy {
        _group.combine(lastWeekTime, ProductivitySummary.Companion::from).asLiveData()
    }

    override fun getApplicableTimers(state: Stopwatch.State): List<Timer> {
        return state.getTimersForSkillIds(group.value?.skills?.map(Skill::id) ?: listOf())
    }

    override suspend fun update(name: String) {
        updateGroup.update(groupId, name, goal.value?.mapToDomain())
    }
}
