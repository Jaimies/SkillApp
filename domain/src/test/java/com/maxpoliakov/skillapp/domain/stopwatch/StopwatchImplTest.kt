package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StopwatchRepository
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch.StateChange
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.time.MutableClock
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import java.time.Duration
import java.time.Instant
import java.time.Instant.EPOCH
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.ChronoUnit.HOURS

class StubSkillRepository : SkillRepository {
    override fun getSkills() = flowOf<List<Skill>>()
    override fun getSkills(criteria: SkillSelectionCriteria) = getSkills()
    override fun getSkillFlowById(id: Id) = flowOf<Skill>()

    override suspend fun getSkillById(id: Id): Skill? {
        if (id == StopwatchImplTest.skillId || id == StopwatchImplTest.otherSkillId)
            return Skill("", MeasurementUnit.Millis, 0, 0, groupId = StopwatchImplTest.groupId)

        return null
    }

    override suspend fun addSkill(skill: Skill) = 1L

    override suspend fun updateName(skillId: Int, newName: String) {}
    override suspend fun updateGoal(skillId: Int, newGoal: Goal?) {}
    override suspend fun deleteSkill(skill: Skill) {}
    override suspend fun updateOrder(skillId: Int, newOrder: Int) {}
    override suspend fun increaseCount(id: Id, count: Long) {}
    override suspend fun decreaseCount(id: Id, count: Long) {}
}

class SpyAddRecordUseCase : AddRecordUseCase {
    private val _addedRecords = mutableListOf<Record>()
    val addedRecords: List<Record> get() = _addedRecords

    override suspend fun run(record: Record): Long {
        _addedRecords.add(record)
        return _addedRecords.size.toLong()
    }

    fun clear() {
        _addedRecords.clear()
    }
}

class StopwatchImplTest : DescribeSpec({
    val addRecord = SpyAddRecordUseCase()
    val notificationUtil = mockk<NotificationUtil>(relaxed = true)
    val skillRepository = StubSkillRepository()
    var clock = MutableClock(EPOCH, UTC)

    beforeEach { clock = MutableClock(EPOCH, UTC) }
    afterEach { addRecord.clear(); clearAllMocks() }

    fun createTimer(startTime: ZonedDateTime = ZonedDateTime.now(clock), skillId: Int = StopwatchImplTest.skillId): Timer {
        return Timer(startTime, skillId, groupId)
    }

    fun createStopwatch(timers: List<Timer> = listOf()): StopwatchImpl {
        val persistence = StubStopwatchRepository(Stopwatch.State(timers))
        return StopwatchImpl(persistence, addRecord, skillRepository, notificationUtil, clock)
    }

    fun createRecord(
        id: Int = 0,
        duration: Duration = Duration.ofSeconds(1),
        date: LocalDate = LocalDate.ofEpochDay(0),
        timeRange: ClosedRange<LocalTime> = LocalTime.ofSecondOfDay(0)..LocalTime.ofSecondOfDay(1)
    ) = Record(
        id = id,
        name = "",
        skillId = skillId,
        count = duration.toMillis(),
        unit = MeasurementUnit.Millis,
        date = date,
        timeRange = timeRange,
    )

    it("reads the initial state from the repository") {
        val firstStopwatch = createStopwatch(timers = listOf())
        firstStopwatch.state.value.timers shouldBe listOf()

        val timer = createTimer(otherStartDateTime)
        val secondStopwatch = createStopwatch(timers = listOf(timer))
        secondStopwatch.state.value.timers shouldBe listOf(timer)
    }

    it("if has a timer, shows the notification on startup") {
       createStopwatch(timers = listOf(createTimer()))
        verify { notificationUtil.showStopwatchNotification(createTimer()) }
    }

    it("if has no timer, removes the notification on startup") {
        createStopwatch(timers = listOf())
        verify { notificationUtil.removeStopwatchNotification() }
    }

    describe("start()") {
        it("if has no timer, adds timer, adds no records, and returns Change.Start with no records") {
            val stopwatch = createStopwatch(timers = listOf())
            stopwatch.start(skillId) shouldBe StateChange.Start(addedRecords = listOf())
            addRecord.addedRecords shouldBe listOf()
            stopwatch.state.value.timers shouldBe listOf(createTimer())
        }

        it("if has a timer and timer.skillId == skillId, does nothing, returns StateChange.None") {
            val timer = createTimer()
            val stopwatch = createStopwatch(timers = listOf(timer))
            clock.withInstant(Instant.ofEpochSecond(1))
            stopwatch.start(skillId) shouldBe StateChange.None
            addRecord.addedRecords shouldBe listOf()
            stopwatch.state.value.timers shouldBe listOf(timer)
        }

        it("if has a timer and timer.skillId != skillId, removes the timer, returns StateChange.Start with the records added, and adds timer") {
            val stopwatch = createStopwatch(timers = listOf(createTimer()))
            clock.withInstant(Instant.ofEpochSecond(1))
            stopwatch.start(otherSkillId) shouldBe StateChange.Start(addedRecords = listOf(createRecord(id = 1)))
            addRecord.addedRecords shouldBe listOf(createRecord())
            stopwatch.state.value.timers shouldBe listOf(createTimer(skillId = otherSkillId))
        }

        it("does not start the timer if the skill with given id does not exist") {
            val stopwatch = createStopwatch(timers = listOf())
            stopwatch.start(nonExistentSkillId) shouldBe StateChange.Start(addedRecords = listOf())
            stopwatch.state.value.timers shouldBe listOf()
        }

        it("persists the state and shows the notification") {
            val persistence = mockk<StopwatchRepository>(relaxed = true)
            val stopwatch = StopwatchImpl(persistence, addRecord, skillRepository, notificationUtil, clock)
            stopwatch.start(skillId)

            val state = Stopwatch.State(listOf(createTimer()))

            verify { persistence.saveState(state) }
            verify { notificationUtil.showStopwatchNotification(state.timers.first()) }
        }
    }

    describe("stop()") {
        it("if has a timer, removes the timer, adds a record, and returns a StateChange.Stop with the record added") {
            val stopwatch = createStopwatch(timers = listOf(createTimer()))
            clock.withInstant(Instant.ofEpochSecond(1))
            stopwatch.stop() shouldBe StateChange.Stop(addedRecords = listOf(createRecord(id = 1)))
            stopwatch.state.value.timers shouldBe listOf()
            addRecord.addedRecords shouldBe listOf(createRecord())
        }

        it("if has no timer, does nothing") {
            val stopwatch = createStopwatch(timers = listOf())
            stopwatch.stop() shouldBe StateChange.None
            stopwatch.state.value.timers shouldBe listOf()
            addRecord.addedRecords shouldBe listOf()
        }

        it("removes the notification") {
            val stopwatch = createStopwatch(timers = listOf(createTimer()))
            stopwatch.stop()
            verify { notificationUtil.removeStopwatchNotification() }
        }

        it("adds multi-day records correctly") {
            clock.withInstant(EPOCH.plus(2, DAYS).plus(10, HOURS))

            val stopwatchUtil = createStopwatch(
                timers = listOf(createTimer(
                    startTime = ZonedDateTime.ofInstant(EPOCH.plus(10, HOURS), UTC),
                )),
            )

            val stateChange = stopwatchUtil.stop()

            val addedRecords = listOf(
                createRecord(
                    date = LocalDate.ofEpochDay(0),
                    duration = Duration.ofHours(14),
                    timeRange = LocalTime.of(10, 0)..LocalTime.MAX,
                ),
                createRecord(
                    date = LocalDate.ofEpochDay(1),
                    duration = Duration.ofDays(1),
                    timeRange = LocalTime.MIN..LocalTime.MAX
                ),
                createRecord(
                    date = LocalDate.ofEpochDay(2),
                    duration = Duration.ofHours(10),
                    timeRange = LocalTime.MIN..LocalTime.of(10, 0)
                ),
            )

            addRecord.addedRecords shouldBe addedRecords
            stateChange.addedRecords shouldBe addedRecords.mapIndexed { index, record ->
                record.copy(id = index + 1)
            }
        }
    }

    describe("toggle()") {
        it("if has no timer, adds a timer") {
            val stopwatch = createStopwatch(timers = listOf())
            stopwatch.toggle(skillId)
            stopwatch.state.value.timers shouldBe listOf(createTimer())
        }

        it("if has a timer and timer.skillId == skillId, stops the stopwatch") {
            val stopwatch = createStopwatch(timers = listOf(createTimer(skillId = skillId)))
            stopwatch.toggle(skillId)
            stopwatch.state.value.timers shouldBe listOf()
        }

        it("if has a timer and timer.skillId != skillId, removes old timer and adds a new one") {
            val stopwatch = createStopwatch(timers = listOf(createTimer(skillId = otherSkillId)))
            stopwatch.toggle(skillId)
            stopwatch.state.value.timers shouldBe listOf(createTimer(skillId = skillId))
        }
    }
}) {
    companion object {
        const val skillId = 12
        const val otherSkillId = 13
        const val nonExistentSkillId = 259
        const val groupId = 5

        private val otherStartDateTime = ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")
    }
}
