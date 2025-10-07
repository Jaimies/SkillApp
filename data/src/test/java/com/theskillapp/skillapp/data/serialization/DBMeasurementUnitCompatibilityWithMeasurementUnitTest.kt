package com.theskillapp.skillapp.data.serialization

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class DBMeasurementUnitCompatibilityWithMeasurementUnitTest : StringSpec({
    "old values of MeasurementUnit are mapped to correct DBMeasurementUnit values" {
        Json.decodeFromString<DBMeasurementUnit>("\"Millis\"") shouldBe DBMeasurementUnit.Millis
        Json.decodeFromString<DBMeasurementUnit>("\"Meters\"") shouldBe DBMeasurementUnit.Meters
        Json.decodeFromString<DBMeasurementUnit>("\"Times\"") shouldBe DBMeasurementUnit.Times
        Json.decodeFromString<DBMeasurementUnit>("\"Pages\"") shouldBe DBMeasurementUnit.Pages
    }
})
