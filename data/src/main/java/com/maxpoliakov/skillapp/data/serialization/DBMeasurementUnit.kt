package com.maxpoliakov.skillapp.data.serialization

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.shared.MappableEnum

enum class DBMeasurementUnit : MappableEnum<DBMeasurementUnit, MeasurementUnit<*>> {
    Millis {
        override fun toDomain() = MeasurementUnit.Millis
    },
    Meters {
        override fun toDomain() = MeasurementUnit.Meters
    },
    Times {
        override fun toDomain() = MeasurementUnit.Times
    };

    companion object : MappableEnum.Companion<DBMeasurementUnit, MeasurementUnit<*>>(values())
}
