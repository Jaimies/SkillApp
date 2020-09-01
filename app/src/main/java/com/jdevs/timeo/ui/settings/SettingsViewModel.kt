package com.jdevs.timeo.ui.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jdevs.timeo.domain.usecase.settings.GetSettingsUseCase
import com.jdevs.timeo.domain.usecase.settings.UpdateSettingsUseCase

class SettingsViewModel @ViewModelInject constructor(
    private val updateSettings: UpdateSettingsUseCase,
    settings: GetSettingsUseCase
) : ViewModel() {
    val activitiesEnabled = settings.activitiesEnabled.asLiveData()

    fun setActivitiesEnabled(isEnabled: Boolean) {
        updateSettings.setActivitiesEnabled(isEnabled)
    }
}
