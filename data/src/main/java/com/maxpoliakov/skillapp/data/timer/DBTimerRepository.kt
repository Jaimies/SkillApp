package com.maxpoliakov.skillapp.data.timer

import com.maxpoliakov.skillapp.domain.di.ApplicationScope
import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.repository.LegacyTimerRepository
import com.maxpoliakov.skillapp.domain.repository.TimerRepository
import com.maxpoliakov.skillapp.shared.util.mapList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class DBTimerRepository @Inject constructor(
    private val timerDao: TimerDao,
    private val legacyTimerRepository: LegacyTimerRepository,
    @ApplicationScope
    private val scope: CoroutineScope,
): TimerRepository {

    init {
        scope.launch {
            migrateTimerFromLegacyTimerRepositoryIfNecessary()
        }
    }

    override fun getAll(): Flow<List<Timer>> {
        return timerDao.getAll().mapList { it.mapToDomain() }
    }

    override suspend fun add(timer: Timer) {
        timerDao.insert(timer.mapToDB())
    }

    override suspend fun remove(timer: Timer) {
        timerDao.delete(timer.mapToDB())
    }

    private suspend fun migrateTimerFromLegacyTimerRepositoryIfNecessary() {
        val timer = legacyTimerRepository.getTimer()
        if (timer != null && this.getAll().first().isEmpty()) {
            this.add(timer)
            legacyTimerRepository.deleteTimer()
        }
    }
}
