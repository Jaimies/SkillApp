package com.jdevs.timeo.data.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

private const val ACTIVITIES_ENABLED = "activitiesEnabled"

@Singleton
class DefaultSettingsRepository @Inject constructor(
    private val localDataSource: LocalUserDataSource,
    private val remoteDataSource: FirestoreUserDataSource
) : SettingsRepository {

    init {

        remoteDataSource.user?.observeForever { user ->

            user?.activitiesEnabled?.let {

                _activitiesEnabled.value = it
                localDataSource.setPreferenceEnabled(ACTIVITIES_ENABLED, it)
            }
        }
    }

    override val activitiesEnabled: LiveData<Boolean> get() = _activitiesEnabled
    private val _activitiesEnabled =
        MutableLiveData(localDataSource.getPreferenceEnabled(ACTIVITIES_ENABLED, false))

    override fun setActivitiesEnabled(isEnabled: Boolean) {

        _activitiesEnabled.value = isEnabled
        remoteDataSource.setPreferenceEnabled(ACTIVITIES_ENABLED, isEnabled)
    }
}
