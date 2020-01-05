package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import org.threeten.bp.OffsetDateTime

interface StatsDataSource {

    val dayStats: LiveData<*>?
    val weekStats: LiveData<*>?
    val monthStats: LiveData<*>?

    suspend fun updateStats(date: OffsetDateTime, time: Long)
}
