package com.maxpoliakov.skillapp.ui.skillgroup

import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import com.maxpoliakov.skillapp.domain.usecase.grouping.GetGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.grouping.UpdateGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetRecentGroupCountUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.ui.common.DetailsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class SkillGroupViewModel @Inject constructor(
    args: SkillGroupFragmentArgs,
    getGroup: GetGroupUseCase,
    stopwatch: Stopwatch,
    getRecentCount: GetRecentGroupCountUseCase,
    private val updateGroup: UpdateGroupUseCase,
) : DetailsViewModel(
    stopwatch,
    getRecentCount,
    getGroup.getById(args.groupId),
) {
    private val groupId = args.groupId

    private val _group = getGroup.getById(groupId)
    val group = _group.asLiveData()

    override val selectionCriteria = SkillSelectionCriteria.InGroupWithId(groupId)

    val summary by lazy {
        _group.combine(lastWeekTime, ProductivitySummary.Companion::from).asLiveData()
    }

    override fun isStopwatchTracking(state: StopwatchState.Running): Boolean {
        return state.groupId == groupId
    }

    override suspend fun update(name: String) {
        updateGroup.update(groupId, name, goal.value?.mapToDomain())
    }
}
