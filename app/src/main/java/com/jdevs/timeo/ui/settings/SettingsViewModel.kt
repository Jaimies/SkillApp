package com.jdevs.timeo.ui.settings

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.domain.usecase.settings.GetSettingsUseCase
import com.jdevs.timeo.domain.usecase.settings.UpdateSettingsUseCase
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    getSettingsUseCase: GetSettingsUseCase
) : ViewModel() {

    val activitiesEnabled = getSettingsUseCase.activitiesEnabled

    fun setActivitiesEnabled(isEnabled: Boolean) {

        updateSettingsUseCase.setActivitiesEnabled(isEnabled)
    }
}
