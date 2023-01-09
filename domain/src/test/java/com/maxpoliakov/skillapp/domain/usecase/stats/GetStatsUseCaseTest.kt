package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.usecase.stub.StubSkillStatsRepository
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import com.maxpoliakov.skillapp.test.await
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.Duration
import java.time.LocalDate

class StubSkillRepository(private vararg val skills: Skill) : SkillRepository {
    override fun getSkills() = flowOf<List<Skill>>()

    override fun getSkills(criteria: SkillSelectionCriteria): Flow<List<Skill>> {
        return flowOf(skills.filter(criteria::isValid))
    }

    override fun getSkillFlowById(id: Id) = flowOf<Skill>()
    override fun getTopSkills(count: Int) = flowOf<List<Skill>>()

    override suspend fun getSkillById(id: Id): Skill? = null

    override suspend fun addSkill(skill: Skill): Long = 1

    override suspend fun updateName(skillId: Int, newName: String) {}
    override suspend fun updateGoal(skillId: Int, newGoal: Goal?) {}

    override suspend fun deleteSkill(skill: Skill) {}

    override suspend fun updateOrder(skillId: Int, newOrder: Int) {}

    override suspend fun increaseCount(id: Id, count: Long) {}
    override suspend fun decreaseCount(id: Id, count: Long) {}
}

class GetStatsUseCaseTest : StringSpec({
    "gets stats for multiple skills" {
        val referenceDate = LocalDate.now().atStartOfWeek()

        val skillStats = listOf(
            Statistic(referenceDate.plusDays(1), Duration.ofHours(2).toMillis()),
            Statistic(referenceDate.plusDays(3), Duration.ofHours(2).toMillis()),
            Statistic(referenceDate.plusDays(4), Duration.ofHours(2).toMillis()),
            Statistic(referenceDate.minusDays(4), Duration.ofHours(2).toMillis()),
            Statistic(referenceDate.minusDays(5), Duration.ofHours(2).toMillis()),
            Statistic(referenceDate.minusDays(15), Duration.ofHours(2).toMillis()),
        )
        val statsRepository = StubSkillStatsRepository(
            mapOf(
                1 to skillStats,
                2 to skillStats,
                3 to skillStats,
            )
        )

        val skillRepository = StubSkillRepository(
            Skill("", MeasurementUnit.Millis, 0, 0, id = 1),
            Skill("", MeasurementUnit.Millis, 0, 0, id = 2),
            Skill("", MeasurementUnit.Millis, 0, 0, id = 3),
        )

        val useCase = GetStatsUseCaseImpl(skillRepository, statsRepository)

        val criteria = SkillSelectionCriteria.WithIdInList(listOf(1, 2))

        useCase.getStats(criteria, LocalDate.now()..LocalDate.now(), StatisticInterval.Weekly).await() shouldBe listOf(
            Statistic(referenceDate, Duration.ofHours(12).toMillis()),
            Statistic(referenceDate.minusWeeks(1), Duration.ofHours(8).toMillis()),
            Statistic(referenceDate.minusWeeks(3), Duration.ofHours(4).toMillis()),
        )
    }
})
