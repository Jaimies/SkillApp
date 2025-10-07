package com.theskillapp.skillapp.data.records

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalTime

class DBRecordTest : StringSpec({
    "mapToDomain() sets timeRange correctly" {
        DBRecord(
            startTime = null,
            endTime = null,
        ).mapToDomain().timeRange shouldBe null

        DBRecord(
            startTime = LocalTime.NOON,
            endTime = null,
        ).mapToDomain().timeRange shouldBe null

        DBRecord(
            startTime = null,
            endTime = LocalTime.NOON,
        ).mapToDomain().timeRange shouldBe null

        DBRecord(
            startTime = LocalTime.NOON,
            endTime = LocalTime.MAX,
        ).mapToDomain().timeRange shouldBe LocalTime.NOON..LocalTime.MAX
    }
})
