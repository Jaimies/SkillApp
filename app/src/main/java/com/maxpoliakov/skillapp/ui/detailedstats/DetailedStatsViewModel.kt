package com.maxpoliakov.skillapp.ui.detailedstats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.util.charts.ChartDataImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DetailedStatsViewModel @Inject constructor(
    skillRepository: SkillRepository,
    chartDataFactory: ChartDataImpl.Factory
) : ViewModel() {
    val startDate = MutableLiveData("")
    val endDate = MutableLiveData("")
    val skillId = MutableLiveData("")

    val allSkills = skillRepository.getSkills()
        .map {
            it
                .sortedBy { skill -> skill.id }
                .joinToString("\n") { skill -> "${skill.id} => ${skill.name}" }
        }
        .asLiveData()

    val chartData = chartDataFactory.create(
        SkillSelectionCriteria.WithUnit(MeasurementUnit.Millis),
        flowOf(MeasurementUnit.Millis),
        flowOf(null)
    )

    fun showStats(): Boolean {
        val startDate = LocalDate.parse(this.startDate.value)
        val endDate = LocalDate.parse(this.endDate.value)

        chartData.coolMutableDate.value = startDate..endDate

        println(startDate)
        println(endDate)

        return false
    }
}
