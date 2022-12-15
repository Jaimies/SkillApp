package com.maxpoliakov.skillapp.shared.range

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalTime

class LocalDateTimeRangeTest : DescribeSpec({
    val endTime = LocalTime.of(12, 0)
    val startTime = LocalTime.of(10, 0)
    describe("split()") {
        it("returns a single part if the range does not go beyond a single day") {
            LocalDate
                .ofEpochDay(0)
                .atTime(startTime)
                .rangeTo(LocalDate.ofEpochDay(0).atTime(endTime))
                .split().shouldBe(
                    listOf(
                        PartOfDateTimeRange(LocalDate.ofEpochDay(0), startTime..endTime),
                    )
                )
        }

        it("returns 2 parts if the range spans over 2 days") {
            LocalDate
                .ofEpochDay(0)
                .atTime(startTime)
                .rangeTo(LocalDate.ofEpochDay(1).atTime(endTime))
                .split().shouldBe(
                    listOf(
                        PartOfDateTimeRange(LocalDate.ofEpochDay(0), startTime..LocalTime.MAX),
                        PartOfDateTimeRange(LocalDate.ofEpochDay(1), LocalTime.MIN..endTime),
                    )
                )
        }

        it("returns multiple parts if the range spans over multiple days") {
            LocalDate
                .ofEpochDay(0)
                .atTime(startTime)
                .rangeTo(LocalDate.ofEpochDay(3).atTime(endTime))
                .split().shouldBe(
                    listOf(
                        PartOfDateTimeRange(LocalDate.ofEpochDay(0), startTime..LocalTime.MAX),
                        PartOfDateTimeRange(LocalDate.ofEpochDay(1), LocalTime.MIN..LocalTime.MAX),
                        PartOfDateTimeRange(LocalDate.ofEpochDay(2), LocalTime.MIN..LocalTime.MAX),
                        PartOfDateTimeRange(LocalDate.ofEpochDay(3), LocalTime.MIN..endTime),
                    )
                )
        }
    }
})
