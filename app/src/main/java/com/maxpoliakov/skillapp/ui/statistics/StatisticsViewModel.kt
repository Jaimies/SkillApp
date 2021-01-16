package com.maxpoliakov.skillapp.ui.statistics

import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.ProductivitySummary
import com.maxpoliakov.skillapp.shared.util.sumByDuration
import com.maxpoliakov.skillapp.ui.stats.StatsViewModel
import com.maxpoliakov.skillapp.util.statistics.getTodayTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    getSkills: GetSkillsUseCase,
    getStats: GetStatsUseCase
) : StatsViewModel(getStats, -1) {

    val summary = getSkills.run().zip(stats) { skills, stats ->
        if (skills.isEmpty())
            return@zip ProductivitySummary.ZERO

        calculateSummary(skills, stats)
    }.asLiveData()
}

fun calculateSummary(skills: List<Skill>, stats: List<Statistic>): ProductivitySummary {
    return ProductivitySummary(
        totalTime = skills.sumByDuration { it.totalTime },
        lastWeekTime = stats.sumByDuration { it.time },
        timeToday = stats.getTodayTime()
    )
}
