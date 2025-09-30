package com.theskillapp.skillapp.domain.repository

import com.theskillapp.skillapp.domain.model.Timer
import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    fun getAll(): Flow<List<Timer>>

    suspend fun add(timer: Timer)
    suspend fun remove(timer: Timer)
}
