package com.jdevs.timeo.data.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSettingsRepository @Inject constructor(
    private val localDataSource: LocalUserDataSource,
    private val remoteDataSource: FirestoreUserDataSource
) : SettingsRepository {
    init {
        remoteDataSource.user?.observeForever { user ->
            _activitiesEnabled.value = user.activitiesEnabled
            localDataSource.activitiesEnabled = user.activitiesEnabled
        }
    }

    override val activitiesEnabled: LiveData<Boolean> get() = _activitiesEnabled
    private val _activitiesEnabled = MutableLiveData(localDataSource.activitiesEnabled)

    override fun setActivitiesEnabled(enabled: Boolean) {
        remoteDataSource.setActivitiesEnabled(enabled)
    }
}
