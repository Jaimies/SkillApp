package com.maxpoliakov.skillapp.domain.repository

import java.time.Duration

interface RemoteConfigRepository {
    fun getAdInterval(): Duration
}
