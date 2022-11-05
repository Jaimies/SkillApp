package com.maxpoliakov.skillapp.ui.skillgroup

import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval.Daily
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.grouping.GetGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.grouping.UpdateGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetRecentGroupCountUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.shared.util.sumByLong
import com.maxpoliakov.skillapp.ui.common.DetailsViewModel
import com.maxpoliakov.skillapp.util.charts.PieData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class SkillGroupViewModel @Inject constructor(
    args: SkillGroupFragmentArgs,
    getGroup: GetGroupUseCase,
    stopwatchUtil: StopwatchUtil,
    getRecentCount: GetRecentGroupCountUseCase,
    private val updateGroup: UpdateGroupUseCase,
) : DetailsViewModel(
    stopwatchUtil,
    getRecentCount,
    getGroup.getById(args.groupId),
) {
    private val groupId = args.groupId

    private val _group = getGroup.getById(groupId)
    val group = _group.asLiveData()

    val pieData = PieData(_group)

    override val selectionCriteria = SkillSelectionCriteria.InGroupWithId(groupId)

    val summary by lazy {
        _group.combine(chartData.getChartData(Daily), ::getSummary).asLiveData()
    }

    private fun getSummary(group: SkillGroup, stats: List<Statistic>): ProductivitySummary {
        return ProductivitySummary(
            group.totalCount,
            stats.sumByLong { it.count },
            group.unit.mapToUI()
        )
    }

    override fun isStopwatchTracking(state: StopwatchState.Running): Boolean {
        return state.groupId == groupId
    }

    override suspend fun update(name: String) {
        updateGroup.update(groupId, name, goal.value?.mapToDomain())
    }
}
