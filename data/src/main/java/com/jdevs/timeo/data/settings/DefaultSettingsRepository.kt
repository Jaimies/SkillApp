package com.jdevs.timeo.data.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.repository.SettingsRepository
import javax.inject.Inject

private const val ACTIVITIES_ENABLED = "activitiesEnabled"

class DefaultSettingsRepository @Inject constructor(
    private val localDataSource: LocalUserDataSource,
    private val firestoreDataSource: FirestoreUserDataSource
) : SettingsRepository {

    init {

        firestoreDataSource.getUser()?.observeForever { user ->

            user?.activitiesEnabled?.let {

                _activitiesEnabled.value = it
                localDataSource.enablePreference(ACTIVITIES_ENABLED, it)
            }
        }
    }

    override val activitiesEnabled: LiveData<Boolean> get() = _activitiesEnabled
    private val _activitiesEnabled =
        MutableLiveData(localDataSource.getPreferenceEnabled(ACTIVITIES_ENABLED, false))

    override fun setActivitiesEnabled(isEnabled: Boolean) {

        _activitiesEnabled.value = isEnabled
        firestoreDataSource.enablePreference(ACTIVITIES_ENABLED, isEnabled)
        localDataSource.enablePreference(ACTIVITIES_ENABLED, isEnabled)
    }
}
