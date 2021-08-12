package com.maxpoliakov.skillapp.ui.skillgroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.grouping.GetGroupUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.shared.util.sumByDuration
import com.maxpoliakov.skillapp.util.charts.toEntries
import com.maxpoliakov.skillapp.util.charts.withMissingStats
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SkillGroupViewModel(
    groupId: Int,
    getGroup: GetGroupUseCase,
    getStats: GetStatsUseCase,
) : ViewModel() {

    private val _group = getGroup.getById(groupId)

    private val _stats = _group
        .flatMapLatest { group -> getStats.run(group.skills.map(Skill::id)) }

    val stats = _stats.map { stats ->
        stats.withMissingStats().toEntries()
    }.asLiveData()

    val group = _group.asLiveData()

    val summary = _group.combine(_stats) { group, stats ->
        ProductivitySummary(group.totalTime, stats.sumByDuration { it.time })
    }.asLiveData()

    class Factory @Inject constructor(
        private val getStats: GetStatsUseCase,
        private val getGroup: GetGroupUseCase,
    ) {
        fun create(groupId: Int) = SkillGroupViewModel(groupId, getGroup, getStats)
    }
}
