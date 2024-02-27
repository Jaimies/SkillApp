package com.maxpoliakov.skillapp.data.serialization

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.shared.MappableEnum

enum class DBMeasurementUnit (override val domainCounterpart: MeasurementUnit<*>): MappableEnum<DBMeasurementUnit, MeasurementUnit<*>> {
    Millis(MeasurementUnit.Millis),
    Meters(MeasurementUnit.Meters),
    Times(MeasurementUnit.Times),
    Pages(MeasurementUnit.Pages),
    Steps(MeasurementUnit.Steps),
    Reps(MeasurementUnit.Reps),
    Kilograms(MeasurementUnit.Kilograms);

    companion object : MappableEnum.Companion<DBMeasurementUnit, MeasurementUnit<*>>(values())
}
