package com.maxpoliakov.skillapp.data.stats

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.shared.util.mapList
import com.maxpoliakov.skillapp.shared.util.sum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StatsRepositoryImpl @Inject constructor(
    private val statsDao: StatsDao,
    private val groupRepository: SkillGroupRepository,
) : StatsRepository {

    override fun getStats(skillId: Id, startDate: LocalDate): Flow<List<Statistic>> {
        val daysAgoStart = ChronoUnit.DAYS.between(startDate, LocalDate.now())
        return statsDao.getStats(skillId, daysAgoStart).mapList { it.mapToDomain() }
    }

    override suspend fun addRecord(record: Record) {
        statsDao.addRecord(record.skillId, record.date, record.time)
    }

    override suspend fun getTimeAtDate(date: LocalDate): Duration {
        return statsDao.getTimeAtDate(date)
    }

    override fun getTimeToday(skillId: Id): Flow<Duration> {
        return statsDao.getTimeAtDate(skillId, LocalDate.now())
            .map { time -> time ?: Duration.ZERO }
    }

    override fun getGroupTimeToday(groupId: Id): Flow<Duration> {
        return groupRepository.getSkillGroupFlowById(groupId).flatMapLatest { group ->
            val timeTodayFlows = group.skills.map { skill ->
                getTimeToday(skill.id)
            }

            combine(timeTodayFlows) { todayTimes -> todayTimes.sum() }
        }
    }
}
