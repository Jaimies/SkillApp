package com.jdevs.timeo.domain.settings

import com.jdevs.timeo.data.settings.SettingsRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    val activitiesEnabled get() = settingsRepository.activitiesEnabled
}
