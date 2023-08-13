package com.maxpoliakov.skillapp.domain.repository

import java.time.LocalTime

interface UserPreferenceRepository {
    fun getDayStartTime(): LocalTime
}
