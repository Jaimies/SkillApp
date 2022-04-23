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
        statsDao.addRecord(record.skillId, record.date, record.count)
    }

    override suspend fun getCountAtDate(date: LocalDate): Long {
        return statsDao.getCountAtDate(date)
    }

    override fun getCountAtDate(skillId: Id, date: LocalDate): Flow<Long> {
        return statsDao.getCountAtDate(skillId, date)
            .map { time -> time ?: 0 }
    }

    override fun getTimeToday(skillId: Id): Flow<Long> {
        return statsDao.getCountAtDate(skillId, LocalDate.now())
            .map { time -> time ?: 0 }
    }

    override fun getGroupTimeAtDate(groupId: Id, date: LocalDate): Flow<Long> {
        return groupRepository.getSkillGroupFlowById(groupId).flatMapLatest { group ->
            val timeTodayFlows = group.skills.map { skill ->
                getCountAtDate(skill.id, date)
            }

            combine(timeTodayFlows) { todayTimes -> todayTimes.sum() }
        }
    }
}
