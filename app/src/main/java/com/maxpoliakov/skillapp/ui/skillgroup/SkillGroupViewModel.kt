package com.maxpoliakov.skillapp.ui.skillgroup

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.grouping.GetGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.grouping.UpdateGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.GetRecordsUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.model.UiGoal.Companion.mapToUI
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.shared.util.sumByLong
import com.maxpoliakov.skillapp.ui.common.DetailsViewModel
import com.maxpoliakov.skillapp.ui.common.GroupChartData
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SkillGroupViewModel(
    private val groupId: Int,
    getGroup: GetGroupUseCase,
    private val getStats: GetStatsUseCase,
    stopwatchUtil: StopwatchUtil,
    getRecords: GetRecordsUseCase,
    private val updateGroup: UpdateGroupUseCase,
) : DetailsViewModel(
    stopwatchUtil,
    getGroup.getById(groupId).map { group -> group.goal },
    getGroup.getById(groupId).flatMapLatest { group ->
        val goal = group.goal ?: return@flatMapLatest flowOf(0L)

        if (goal.type == Goal.Type.Daily) getStats.getGroupTimeToday(groupId)
        else getStats.getGroupTimeThisWeek(groupId)
    },
) {

    private val _group = getGroup.getById(groupId)
    override val nameFlow = _group.map { it.name }

    private val dailyStats = _group
        .flatMapLatest { group -> getStats.getDailyStats(group.skills.map(Skill::id)) }

    val chartData = GroupChartData(getStats, getGroup, groupId)

    val group = _group.asLiveData()
    override val unit = group.map { group -> group.unit.mapToUI() }

    val uiGoal = group.map { group -> group.goal?.mapToUI(group.unit) }

    val summary = _group.combine(dailyStats) { group, stats ->
        ProductivitySummary(group.totalCount, stats.sumByLong { it.count }, group.unit.mapToUI())
    }.asLiveData()

    override val recordPagingData = _group
        .map { group -> group.skills.map(Skill::id) }
        .distinctUntilChanged()
        .flatMapLatest(getRecords::run)

    override fun isStopwatchTracking(state: StopwatchState.Running): Boolean {
        return state.groupId == groupId
    }

    override suspend fun update(name: String) {
        updateGroup.update(groupId, name, goal.value)
    }

    class Factory @Inject constructor(
        private val getStats: GetStatsUseCase,
        private val getGroup: GetGroupUseCase,
        private val updateGroup: UpdateGroupUseCase,
        private val stopwatchUtil: StopwatchUtil,
        private val getRecords: GetRecordsUseCase,
    ) {
        fun create(groupId: Int) = SkillGroupViewModel(groupId, getGroup, getStats, stopwatchUtil, getRecords, updateGroup)
    }
}
