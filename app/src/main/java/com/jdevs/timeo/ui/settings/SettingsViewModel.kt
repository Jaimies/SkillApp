package com.jdevs.timeo.ui.settings

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.domain.usecase.settings.GetSettingsUseCase
import com.jdevs.timeo.domain.usecase.settings.UpdateSettingsUseCase
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val updateSettings: UpdateSettingsUseCase,
    settings: GetSettingsUseCase
) : ViewModel() {

    val activitiesEnabled = settings.activitiesEnabled

    fun setActivitiesEnabled(isEnabled: Boolean) {

        updateSettings.setActivitiesEnabled(isEnabled)
    }
}
