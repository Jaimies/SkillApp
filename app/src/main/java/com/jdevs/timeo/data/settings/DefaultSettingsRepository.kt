package com.jdevs.timeo.data.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.repository.SettingsRepository
import javax.inject.Inject

class DefaultSettingsRepository @Inject constructor(
    private val sharedPrefs: SharedPreferences,
    private val userDataSource: UserDataSource
) : SettingsRepository {

    init {

        userDataSource.getUser().observeForever { user ->

            user?.let {

                sharedPrefs.edit().putBoolean(ACTIVITIES_ENABLED_PROP, it.activitiesEnabled).apply()
            }
        }
    }

    override val activitiesEnabled =
        MutableLiveData(sharedPrefs.getBoolean(ACTIVITIES_ENABLED_PROP, false))

    override fun setActivitiesEnabled(isEnabled: Boolean) {

        userDataSource.updateUserPreferences(ACTIVITIES_ENABLED_PROP, isEnabled)
        sharedPrefs.edit().putBoolean(ACTIVITIES_ENABLED_PROP, isEnabled).apply()
    }

    companion object {

        private const val ACTIVITIES_ENABLED_PROP = "activitiesEnabled"
    }
}
