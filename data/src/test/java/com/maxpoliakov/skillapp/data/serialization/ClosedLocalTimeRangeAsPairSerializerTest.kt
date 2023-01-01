package com.maxpoliakov.skillapp.data.serialization

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import java.time.LocalTime

class ClosedLocalTimeRangeAsPairSerializerTest : StringSpec({
    "encode" {
        Json.encodeToString(
            ClosedLocalTimeRangeAsPairSerializer,
            LocalTime.of(8, 40)..LocalTime.of(9, 20),
        ) shouldBe """{"first":"08:40:00","second":"09:20:00"}"""
    }

    "decode" {
        Json.decodeFromString(
            ClosedLocalTimeRangeAsPairSerializer,
            """{"first":"08:40:00","second":"09:20:00"}""",
        ) shouldBe LocalTime.of(8, 40)..LocalTime.of(9, 20)
    }
})
