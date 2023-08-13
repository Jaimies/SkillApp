package com.maxpoliakov.skillapp.shared.range

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalTime

class LocalDateTimeRangeTest : DescribeSpec({
    describe("split(dayStartTime = LocalTime.MIN)") {
        val startTime = LocalTime.of(10, 0)
        val endTime = LocalTime.of(12, 0)

        it("start and end on the same day -> single part") {
            LocalDate
                .ofEpochDay(0)
                .atTime(startTime)
                .rangeTo(LocalDate.ofEpochDay(0).atTime(endTime))
                .split(LocalTime.MIN).shouldBe(
                    listOf(
                        PartOfDateTimeRange(LocalDate.ofEpochDay(0), startTime..endTime),
                    )
                )
        }

        it("end date = start date + 1 day -> two parts") {
            LocalDate
                .ofEpochDay(0)
                .atTime(startTime)
                .rangeTo(LocalDate.ofEpochDay(1).atTime(endTime))
                .split(LocalTime.MIN).shouldBe(
                    listOf(
                        PartOfDateTimeRange(LocalDate.ofEpochDay(0), startTime..LocalTime.MAX),
                        PartOfDateTimeRange(LocalDate.ofEpochDay(1), LocalTime.MIN..endTime),
                    )
                )
        }

        it("range spans over multiple days -> multiple parts") {
            LocalDate
                .ofEpochDay(0)
                .atTime(startTime)
                .rangeTo(LocalDate.ofEpochDay(3).atTime(endTime))
                .split(LocalTime.MIN).shouldBe(
                    listOf(
                        PartOfDateTimeRange(LocalDate.ofEpochDay(0), startTime..LocalTime.MAX),
                        PartOfDateTimeRange(LocalDate.ofEpochDay(1), LocalTime.MIN..LocalTime.MAX),
                        PartOfDateTimeRange(LocalDate.ofEpochDay(2), LocalTime.MIN..LocalTime.MAX),
                        PartOfDateTimeRange(LocalDate.ofEpochDay(3), LocalTime.MIN..endTime),
                    )
                )
        }
    }

    describe("split(dayStartTime = 11:00:00)") {
        val dayStartTime = LocalTime.of(11, 0)
        val dayEndTime = LocalTime.of(10, 59, 59, 999999999)

        describe("start and end on the same day") {
            it("start and end before day start -> returns a single part for the day before") {
                val startTime = LocalTime.of(5, 0)
                val endTime = LocalTime.of(9, 0)

                LocalDate
                    .ofEpochDay(0)
                    .atTime(startTime)
                    .rangeTo(LocalDate.ofEpochDay(0).atTime(endTime))
                    .split(dayStartTime).shouldBe(
                        listOf(
                            PartOfDateTimeRange(LocalDate.ofEpochDay(-1), startTime..endTime),
                        )
                    )
            }

            it("start and end after day start -> returns a single part for the current day") {
                val startTime = LocalTime.of(12, 0)
                val endTime = LocalTime.of(13, 0)

                LocalDate
                    .ofEpochDay(0)
                    .atTime(startTime)
                    .rangeTo(LocalDate.ofEpochDay(0).atTime(endTime))
                    .split(dayStartTime).shouldBe(
                        listOf(
                            PartOfDateTimeRange(LocalDate.ofEpochDay(0), startTime..endTime),
                        )
                    )
            }

            it("start before day start and end after day start -> returns 2 parts, one for previous day, one for current") {
                val startTime = LocalTime.of(10, 0)
                val endTime = LocalTime.of(12, 0)

                LocalDate
                    .ofEpochDay(0)
                    .atTime(startTime)
                    .rangeTo(LocalDate.ofEpochDay(0).atTime(endTime))
                    .split(dayStartTime).shouldBe(
                        listOf(
                            PartOfDateTimeRange(LocalDate.ofEpochDay(-1), startTime..dayEndTime),
                            PartOfDateTimeRange(LocalDate.ofEpochDay(0), dayStartTime..endTime),
                        )
                    )
            }
        }

        it("end date = start date + 1 day, start time is before day start, end time is after day start -> 3 parts") {
            val startTime = LocalTime.of(5, 0)
            val endTime = LocalTime.of(12, 0)

            LocalDate
                .ofEpochDay(0)
                .atTime(startTime)
                .rangeTo(LocalDate.ofEpochDay(1).atTime(endTime))
                .split(dayStartTime).shouldBe(
                    listOf(
                        PartOfDateTimeRange(LocalDate.ofEpochDay(-1), startTime..dayEndTime),
                        PartOfDateTimeRange(LocalDate.ofEpochDay(0), dayStartTime..dayEndTime),
                        PartOfDateTimeRange(LocalDate.ofEpochDay(1), dayStartTime..endTime),
                    )
                )
        }

        it("range spans over multiple days -> multiple parts") {
            val startTime = LocalTime.of(10, 0)
            val endTime = LocalTime.of(12, 0)

            LocalDate
                .ofEpochDay(0)
                .atTime(startTime)
                .rangeTo(LocalDate.ofEpochDay(2).atTime(endTime))
                .split(dayStartTime)
                .shouldBe(
                    listOf(
                        PartOfDateTimeRange(LocalDate.ofEpochDay(-1), startTime..dayEndTime),
                        PartOfDateTimeRange(LocalDate.ofEpochDay(0), dayStartTime..dayEndTime),
                        PartOfDateTimeRange(LocalDate.ofEpochDay(1), dayStartTime..dayEndTime),
                        PartOfDateTimeRange(LocalDate.ofEpochDay(2), dayStartTime..endTime),
                    )
                )
        }
    }
})
