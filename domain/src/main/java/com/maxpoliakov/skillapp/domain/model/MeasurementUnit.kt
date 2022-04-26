package com.maxpoliakov.skillapp.domain.model

enum class MeasurementUnit(val canBeGrouped: Boolean) {
    Millis(true), Meters(false), Times(false);
}
