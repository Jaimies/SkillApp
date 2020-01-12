package com.jdevs.timeo.domain.settings

import com.jdevs.timeo.data.settings.SettingsRepository
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    fun setActivitiesEnabled(isEnabled: Boolean) =
        settingsRepository.setActivitiesEnabled(isEnabled)
}
