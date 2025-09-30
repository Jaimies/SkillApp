package com.theskillapp.skillapp.data.serialization

import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.shared.MappableEnum

enum class DBMeasurementUnit (override val domainCounterpart: MeasurementUnit<*>): MappableEnum<DBMeasurementUnit, MeasurementUnit<*>> {
    Millis(MeasurementUnit.Millis),
    Meters(MeasurementUnit.Meters),
    Times(MeasurementUnit.Times),
    Pages(MeasurementUnit.Pages),
    Steps(MeasurementUnit.Steps),
    Reps(MeasurementUnit.Reps),
    Kilograms(MeasurementUnit.Kilograms),
    Calories(MeasurementUnit.Calories);

    companion object : MappableEnum.Companion<DBMeasurementUnit, MeasurementUnit<*>>(values())
}
