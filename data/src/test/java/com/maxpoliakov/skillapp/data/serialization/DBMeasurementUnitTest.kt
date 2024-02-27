package com.maxpoliakov.skillapp.data.serialization

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.spec.style.StringSpec

class DBMeasurementUnitTest : StringSpec({
    "has a value for each value of MeasurementUnit" {
        val measurementUnitValues = MeasurementUnit::class.nestedClasses
            .filter { it.isFinal }
            .map { it.objectInstance!! } as List<MeasurementUnit<*>>

        for (unit in measurementUnitValues) {
            shouldNotThrow<IllegalArgumentException> { DBMeasurementUnit.from(unit) }
        }
    }
})
