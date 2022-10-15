package com.maxpoliakov.skillapp.ui.skillgroup

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.StatisticInterval.Daily
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.grouping.GetGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.grouping.UpdateGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.GetRecordsUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetRecentGroupCountUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetTimeAtDateUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.model.UiGoal.Companion.mapToUI
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.shared.util.sumByLong
import com.maxpoliakov.skillapp.ui.common.DetailsViewModel
import com.maxpoliakov.skillapp.ui.common.GroupChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SkillGroupViewModel @Inject constructor(
    args: SkillGroupFragmentArgs,
    getGroup: GetGroupUseCase,
    getStats: GetStatsUseCase,
    stopwatchUtil: StopwatchUtil,
    getRecords: GetRecordsUseCase,
    getRecentCount: GetRecentGroupCountUseCase,
    private val getTimeAtDate: GetTimeAtDateUseCase,
    private val updateGroup: UpdateGroupUseCase,
) : DetailsViewModel(
    stopwatchUtil,
    getRecentCount,
    getGroup.getById(args.groupId),
) {
    private val groupId = args.groupId

    private val _group = getGroup.getById(groupId)
    val group = _group.asLiveData()

    override val nameFlow = _group.map { it.name }

    val chartData = GroupChartData(getStats, getGroup, groupId)

    override val unitFlow = _group.map { group -> group.unit.mapToUI() }

    val uiGoal = group.map { group -> group.goal?.mapToUI(group.unit) }

    val summary = _group.combine(chartData.getChartData(Daily)) { group, stats ->
        ProductivitySummary(group.totalCount, stats.sumByLong { it.count }, group.unit.mapToUI())
    }.asLiveData()

    override val recordPagingData = _group
        .map { group -> group.skills.map(Skill::id) }
        .distinctUntilChanged()
        .flatMapLatest(getRecords::run)

    override suspend fun getTimeAtDate(date: LocalDate): Duration {
        return getTimeAtDate.run(_group.first().skills.map { it.id }, date)
    }

    override fun isStopwatchTracking(state: StopwatchState.Running): Boolean {
        return state.groupId == groupId
    }

    override suspend fun update(name: String) {
        updateGroup.update(groupId, name, goal.value?.mapToDomain())
    }
}
