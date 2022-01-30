package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.AddSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Duration
import java.time.LocalDate
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltAndroidTest
class GetStatsUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getStats: GetStatsUseCase

    @Inject
    lateinit var addRecord: AddRecordUseCase

    @Inject
    lateinit var addSkill: AddSkillUseCase

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun works() = runBlocking<Unit> {
        addSkill.run(Skill("", Duration.ZERO, Duration.ZERO, id = 1))
        addSkill.run(Skill("", Duration.ZERO, Duration.ZERO, id = 2))
        addSkill.run(Skill("", Duration.ZERO, Duration.ZERO, id = 3))
        addSkill.run(Skill("", Duration.ZERO, Duration.ZERO, id = 4))
        addSkill.run(Skill("", Duration.ZERO, Duration.ZERO, id = 5))
        addSkill.run(Skill("", Duration.ZERO, Duration.ZERO, id = 6))

        repeat(210) { index ->
            val date = LocalDate.now().minusDays(index.toLong())
            addRecord.run(Record("", 1, Duration.ofMinutes(1), 0, date))
            addRecord.run(Record("", 2, Duration.ofMinutes(1), 0, date))
            addRecord.run(Record("", 3, Duration.ofMinutes(1), 0, date))
            addRecord.run(Record("", 4, Duration.ofMinutes(1), 0, date))
            addRecord.run(Record("", 5, Duration.ofMinutes(1), 0, date))
            addRecord.run(Record("", 6, Duration.ofMinutes(1), 0, date))
        }

        val time = measureTimeMillis {
            getStats.getMonthlyStats(listOf(1, 2, 3, 4, 5, 6))
        }

        println(time)
    }
}
