package com.jdevs.timeo.data.settings

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val ACTIVITIES_ENABLED = "activitiesEnabled"

interface UserDataSource {
    var activitiesEnabled: Boolean
}

@Singleton
class DefaultUserDataSource @Inject constructor(@ApplicationContext context: Context) :
    UserDataSource {

    private val prefs = getDefaultSharedPreferences(context)

    override var activitiesEnabled: Boolean
        get() = prefs.getBoolean(ACTIVITIES_ENABLED, true)
        set(value) = prefs.edit { putBoolean(ACTIVITIES_ENABLED, value) }
}
