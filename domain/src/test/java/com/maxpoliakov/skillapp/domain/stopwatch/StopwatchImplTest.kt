package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch.StateChange
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.time.MutableClock
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineScope
import java.time.Duration
import java.time.Instant
import java.time.Instant.EPOCH
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit.DAYS
import java.time.temporal.ChronoUnit.HOURS

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
    var clock = MutableClock(EPOCH, UTC)

    beforeEach { clock = MutableClock(EPOCH, UTC) }
    afterEach { addRecord.clear(); clearAllMocks() }

    fun createTimer(startTime: ZonedDateTime = ZonedDateTime.now(clock), skillId: Int = StopwatchImplTest.skillId): Timer {
        return Timer(skillId, startTime)
    }

    fun createStopwatch(timers: List<Timer> = listOf()): StopwatchImpl {
        val repository = StubTimerRepository(timers)
        return StopwatchImpl(repository, addRecord, notificationUtil, CoroutineScope(Dispatchers.IO), clock)
    }

    fun createRecord(
        id: Int = 0,
        skillId: Int = StopwatchImplTest.skillId,
        duration: Duration = Duration.ofSeconds(1),
        date: LocalDate = LocalDate.ofEpochDay(0),
        timeRange: ClosedRange<LocalTime> = LocalTime.ofSecondOfDay(0)..LocalTime.ofSecondOfDay(1),
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
        firstStopwatch.state.first().timers shouldBe listOf()

        val timer = createTimer(otherStartDateTime)
        val secondStopwatch = createStopwatch(timers = listOf(timer))
        secondStopwatch.state.first().timers shouldBe listOf(timer)
    }

    it("updates notification on startup") {
        createStopwatch(timers = listOf(createTimer()))
        verify { notificationUtil.updateTimerNotifications(listOf(createTimer())) }
    }

    describe("start()") {
        it("if has no timer, adds timer, adds no records, and returns Change.Start with no records") {
            val stopwatch = createStopwatch(timers = listOf())
            stopwatch.start(skillId) shouldBe StateChange.Start
            addRecord.addedRecords shouldBe listOf()
            stopwatch.state.first().timers shouldBe listOf(createTimer())
        }

        it("if has a timer and timer.skillId == skillId, does nothing, returns StateChange.None") {
            val timer = createTimer()
            val stopwatch = createStopwatch(timers = listOf(timer))
            clock.withInstant(Instant.ofEpochSecond(1))
            stopwatch.start(skillId) shouldBe StateChange.None
            addRecord.addedRecords shouldBe listOf()
            stopwatch.state.first().timers shouldBe listOf(timer)
        }

        it("if has a timer and timer.skillId != skillId, adds timer, adds no records, returns StateChange.Start") {
            val timer = createTimer(skillId = skillId)
            val stopwatch = createStopwatch(timers = listOf(timer))
            clock.withInstant(Instant.ofEpochSecond(1))
            stopwatch.start(otherSkillId) shouldBe StateChange.Start
            addRecord.addedRecords shouldBe listOf()
            stopwatch.state.first().timers shouldBe listOf(timer, createTimer(skillId = otherSkillId))
        }

        it("shows the notification") {
            val repository = StubTimerRepository(listOf())
            val stopwatch = StopwatchImpl(repository, addRecord, notificationUtil, TestCoroutineScope(), clock)
            stopwatch.start(skillId)
            verify { notificationUtil.updateTimerNotifications(listOf(createTimer())) }
        }
    }

    describe("stop()") {
        it("if has a single timer, removes the timer, adds a record, and returns a StateChange.Stop with the record added") {
            val stopwatch = createStopwatch(timers = listOf(createTimer()))
            clock.withInstant(Instant.ofEpochSecond(1))
            stopwatch.stop(skillId) shouldBe StateChange.Stop(addedRecords = listOf(createRecord(id = 1)))
            stopwatch.state.first().timers shouldBe listOf()
            addRecord.addedRecords shouldBe listOf(createRecord())
        }

        it("if has multiple timers, removes the one for given skill id, adds a record, returns StateChange.Stop with the records added") {
            val timer = createTimer(skillId = skillId)
            val otherTimer = createTimer(skillId = otherSkillId)
            val stopwatch = createStopwatch(timers = listOf(timer, otherTimer))
            clock.withInstant(Instant.ofEpochSecond(1))
            stopwatch.stop(otherSkillId) shouldBe StateChange.Stop(addedRecords = listOf(createRecord(id = 1, skillId = otherSkillId)))
            stopwatch.state.first().timers shouldBe listOf(timer)
        }

        it("if has no timer, does nothing") {
            val stopwatch = createStopwatch(timers = listOf())
            stopwatch.stop(skillId) shouldBe StateChange.None
            stopwatch.state.first().timers shouldBe listOf()
            addRecord.addedRecords shouldBe listOf()
        }

        it("updates the notification") {
            val stopwatch = createStopwatch(timers = listOf(createTimer()))
            stopwatch.stop(skillId)
            verify { notificationUtil.updateTimerNotifications(listOf()) }
        }

        it("adds multi-day records correctly") {
            clock.withInstant(EPOCH.plus(2, DAYS).plus(10, HOURS))

            val stopwatchUtil = createStopwatch(
                timers = listOf(createTimer(
                    startTime = ZonedDateTime.ofInstant(EPOCH.plus(10, HOURS), UTC),
                )),
            )

            val stateChange = stopwatchUtil.stop(skillId)

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
            stopwatch.state.first().timers shouldBe listOf(createTimer())
        }

        it("if has a timer and timer.skillId == skillId, stops the stopwatch") {
            val stopwatch = createStopwatch(timers = listOf(createTimer(skillId = skillId)))
            stopwatch.toggle(skillId)
            stopwatch.state.first().timers shouldBe listOf()
        }

        it("if has a timer and timer.skillId != skillId, starts a new one") {
            val timer = createTimer(skillId = otherSkillId)
            val stopwatch = createStopwatch(timers = listOf(timer))
            stopwatch.toggle(skillId)
            stopwatch.state.first().timers shouldBe listOf(timer, createTimer(skillId = skillId))
        }
    }
}) {
    companion object {
        const val skillId = 12
        const val otherSkillId = 13
        const val nonExistentSkillId = 259

        private val otherStartDateTime = ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")
    }
}
