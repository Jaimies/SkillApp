package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Timer

interface LegacyTimerRepository {
    fun getTimer(): Timer?
    fun deleteTimer()
}
