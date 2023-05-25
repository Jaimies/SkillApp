package com.maxpoliakov.skillapp.data.timer

import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.repository.TimerRepository
import com.maxpoliakov.skillapp.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DBTimerRepository @Inject constructor(
    private val timerDao: TimerDao,
): TimerRepository {
    override fun getAll(): Flow<List<Timer>> {
        return timerDao.getAll().mapList { it.mapToDomain() }
    }

    override suspend fun add(timer: Timer) {
        timerDao.insert(timer.mapToDB())
    }

    override suspend fun remove(timer: Timer) {
        timerDao.delete(timer.mapToDB())
    }
}
