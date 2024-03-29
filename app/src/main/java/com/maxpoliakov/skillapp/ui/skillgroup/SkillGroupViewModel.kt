package com.maxpoliakov.skillapp.ui.skillgroup

import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import com.maxpoliakov.skillapp.domain.usecase.grouping.GetGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.grouping.UpdateGroupUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.shared.DetailsViewModel
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
