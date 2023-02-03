package com.maxpoliakov.skillapp.domain.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalTime

class RecordChangeTest : DescribeSpec({
    describe("TimeRange") {
        it("does not change the record if timeRange is null") {
            recordRangeChange.apply(recordWithoutTimeRange) shouldBe recordWithoutTimeRange
        }

        it("applies the change to timeRange if it is not null") {
            recordRangeChange.apply(record) shouldBe record.copy(timeRange = newTime..endTime)
        }
    }
}) {
    companion object {
        private val startTime = LocalTime.of(10, 0)
        private val endTime = LocalTime.of(12, 15)
        private val newTime = LocalTime.of(11, 0)

        private val record = Record(
            name = "name",
            skillId = 2,
            count = 1000,
            unit = MeasurementUnit.Meters,
            date = LocalDate.ofEpochDay(0),
            timeRange = startTime..endTime,
        )

        private val recordWithoutTimeRange = record.copy(timeRange = null)

        private val rangeChange = RangeChange.Start(newTime)
        private val recordRangeChange = RecordChange.TimeRange(rangeChange)
    }
}
