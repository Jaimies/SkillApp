package com.jdevs.timeo.data.settings

import com.jdevs.timeo.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSettingsRepository @Inject constructor(
    private val dataSource: UserDataSource
) : SettingsRepository {
    override val activitiesEnabled: Flow<Boolean> =
        flowOf(dataSource.activitiesEnabled)

    override fun setActivitiesEnabled(enabled: Boolean) {
        dataSource.activitiesEnabled = enabled
    }
}
