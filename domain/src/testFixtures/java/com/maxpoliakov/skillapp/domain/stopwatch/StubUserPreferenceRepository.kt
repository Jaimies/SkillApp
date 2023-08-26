package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.repository.UserPreferenceRepository
import java.time.LocalTime

class StubUserPreferenceRepository(
    @get:JvmName("_getDayStartTime")
    var dayStartTime: LocalTime = LocalTime.MIDNIGHT,
) : UserPreferenceRepository {
    override fun getDayStartTime() = dayStartTime
}
