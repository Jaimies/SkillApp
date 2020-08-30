package com.jdevs.timeo.data.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSettingsRepository @Inject constructor(
    private val dataSource: UserDataSource
) : SettingsRepository {
    override val activitiesEnabled: LiveData<Boolean> get() = _activitiesEnabled
    private val _activitiesEnabled =
        MutableLiveData(dataSource.activitiesEnabled)

    override fun setActivitiesEnabled(enabled: Boolean) {
        dataSource.activitiesEnabled = enabled
    }
}
