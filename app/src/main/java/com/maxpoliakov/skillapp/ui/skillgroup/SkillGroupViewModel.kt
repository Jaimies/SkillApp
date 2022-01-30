package com.maxpoliakov.skillapp.ui.skillgroup

import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.grouping.GetGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.grouping.UpdateGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.shared.util.sumByDuration
import com.maxpoliakov.skillapp.ui.common.DetailsViewModel
import com.maxpoliakov.skillapp.ui.common.GroupChartData
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SkillGroupViewModel(
    private val groupId: Int,
    getGroup: GetGroupUseCase,
    private val getStats: GetStatsUseCase,
    stopwatchUtil: StopwatchUtil,
    private val updateGroup: UpdateGroupUseCase,
) : DetailsViewModel(
    stopwatchUtil,
    getGroup.getById(groupId).map { group -> group.goal },
    getStats.getGroupTimeToday(groupId),
) {

    private val _group = getGroup.getById(groupId)
    override val nameFlow = _group.map { it.name }

    private val dailyStats = _group
        .flatMapLatest { group -> getStats.getDailyStats(group.skills.map(Skill::id)) }

    val chartData = GroupChartData(getStats, getGroup, groupId)

    val group = _group.asLiveData()

    val summary = _group.combine(dailyStats) { group, stats ->
        ProductivitySummary(group.totalTime, stats.sumByDuration { it.time })
    }.asLiveData()

    override suspend fun update(name: String) {
        updateGroup.update(groupId, name, goal.value)
    }

    class Factory @Inject constructor(
        private val getStats: GetStatsUseCase,
        private val getGroup: GetGroupUseCase,
        private val updateGroup: UpdateGroupUseCase,
        private val stopwatchUtil: StopwatchUtil,
    ) {
        fun create(groupId: Int) = SkillGroupViewModel(groupId, getGroup, getStats, stopwatchUtil, updateGroup)
    }
}
