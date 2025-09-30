package com.theskillapp.skillapp.domain.repository

import com.theskillapp.skillapp.domain.model.Timer

interface LegacyTimerRepository {
    fun getTimer(): Timer?
    fun deleteTimer()
}
