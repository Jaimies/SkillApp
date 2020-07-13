package com.jdevs.timeo.data.settings

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface LocalUserDataSource {

    var activitiesEnabled: Boolean
}

@Singleton
class DefaultLocalUserDataSource @Inject constructor(@ApplicationContext context: Context) :
    LocalUserDataSource {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    override var activitiesEnabled: Boolean
        get() = prefs.getBoolean(ACTIVITIES_ENABLED, true)
        set(value) = prefs.edit { putBoolean(ACTIVITIES_ENABLED, value) }
}
