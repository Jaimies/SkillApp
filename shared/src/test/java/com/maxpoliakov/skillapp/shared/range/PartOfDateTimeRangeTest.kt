package com.maxpoliakov.skillapp.shared.range

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class PartOfDateTimeRangeTest : DescribeSpec({
    describe("toDuration()") {
        it("returns the duration between the times") {
            PartOfDateTimeRange(
                LocalDate.ofEpochDay(0), LocalTime.of(0, 0)..LocalTime.of(1, 0),
            ).toDuration() shouldBe Duration.ofHours(1)

            PartOfDateTimeRange(
                LocalDate.ofEpochDay(0), LocalTime.of(10, 0)..LocalTime.of(15, 30),
            ).toDuration() shouldBe Duration.ofHours(5).plusMinutes(30)
        }

        it("returns the correct duration when the end value of the range is LocalTime.MAX") {
            PartOfDateTimeRange(
                LocalDate.ofEpochDay(0), LocalTime.of(20, 0)..LocalTime.MAX,
            ).toDuration() shouldBe Duration.ofHours(4)
        }
    }
})
