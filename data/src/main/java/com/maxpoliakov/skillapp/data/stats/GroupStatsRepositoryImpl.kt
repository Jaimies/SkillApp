package com.maxpoliakov.skillapp.data.stats

import com.maxpoliakov.skillapp.data.group.GroupDao
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.repository.GroupStatsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.shared.util.sumByLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import java.time.LocalDate
import javax.inject.Inject

class GroupStatsRepositoryImpl @Inject constructor(
    private val groupDao: GroupDao,
    private val skillStatsRepository: SkillStatsRepository,
) : GroupStatsRepository {

    override fun getCountAtDateFlow(id: Id, date: LocalDate): Flow<Long> {
        return groupDao.getGroupFlowById(id).flatMapLatest { group ->
            val timeTodayFlows = group.skills.map { (id) ->
                skillStatsRepository.getCountAtDateFlow(id, date)
            }

            combine(timeTodayFlows) { todayTimes -> todayTimes.sum() }
        }
    }

    override suspend fun getCountAtDate(id: Id, date: LocalDate): Long {
        return groupDao.getGroupById(id)?.skills?.sumByLong { skill ->
            skillStatsRepository.getCountAtDate(skill.id, date)
        } ?: 0
    }
}
