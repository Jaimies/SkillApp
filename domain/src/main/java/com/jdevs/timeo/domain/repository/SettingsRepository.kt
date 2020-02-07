package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData

interface SettingsRepository {

    val activitiesEnabled: LiveData<Boolean>

    fun setActivitiesEnabled(isEnabled: Boolean)
}
