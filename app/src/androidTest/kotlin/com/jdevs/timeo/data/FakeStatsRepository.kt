package com.jdevs.timeo.data

import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.model.Statistic
import com.jdevs.timeo.domain.repository.StatsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeStatsRepository @Inject constructor() : StatsRepository {
    private val dayStatsList = mutableListOf<Statistic>()
    private val weekStatsList = mutableListOf<Statistic>()
    private val monthStatsList = mutableListOf<Statistic>()

    override val dayStats = MutableLiveData(dayStatsList.toList())
    override val weekStats = MutableLiveData(weekStatsList.toList())
    override val monthStats = MutableLiveData(monthStatsList.toList())
}
