package com.maxpoliakov.skillapp.domain.model

class Distance private constructor(
    val meters: Long,
) {
    companion object {
        fun ofMeters(meters: Long): Distance = Distance(meters)
    }
}
