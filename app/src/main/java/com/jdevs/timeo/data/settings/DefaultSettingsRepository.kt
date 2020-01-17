package com.jdevs.timeo.data.settings

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.repository.SettingsRepository
import javax.inject.Inject

class DefaultSettingsRepository @Inject constructor(
    private val sharedPrefs: SharedPreferences,
    private val userDataSource: UserDataSource
) : SettingsRepository {

    init {

        userDataSource.getUser()?.observeForever { user ->

            user?.activitiesEnabled?.let {

                _activitiesEnabled.value = it
                sharedPrefs.edit().putBoolean(ACTIVITIES_ENABLED, it).apply()
            }
        }
    }

    override val activitiesEnabled: LiveData<Boolean> get() = _activitiesEnabled
    private val _activitiesEnabled =
        MutableLiveData(sharedPrefs.getBoolean(ACTIVITIES_ENABLED, false))

    override fun setActivitiesEnabled(isEnabled: Boolean) {

        _activitiesEnabled.value = isEnabled
        userDataSource.updateUserPreferences(ACTIVITIES_ENABLED, isEnabled)
        sharedPrefs.edit().putBoolean(ACTIVITIES_ENABLED, isEnabled).apply()
    }

    companion object {

        private const val ACTIVITIES_ENABLED = "activitiesEnabled"
    }
}
