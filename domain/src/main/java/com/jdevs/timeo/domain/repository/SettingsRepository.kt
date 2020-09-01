package com.jdevs.timeo.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val activitiesEnabled: Flow<Boolean>

    fun setActivitiesEnabled(enabled: Boolean)
}
