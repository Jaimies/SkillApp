package com.jdevs.timeo.domain.usecase.settings

import com.jdevs.timeo.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    fun setActivitiesEnabled(isEnabled: Boolean) =
        settingsRepository.setActivitiesEnabled(isEnabled)
}
