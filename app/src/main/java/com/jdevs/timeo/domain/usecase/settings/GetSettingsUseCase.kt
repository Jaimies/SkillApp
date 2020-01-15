package com.jdevs.timeo.domain.usecase.settings

import com.jdevs.timeo.domain.repository.SettingsRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    val activitiesEnabled get() = settingsRepository.activitiesEnabled
}
