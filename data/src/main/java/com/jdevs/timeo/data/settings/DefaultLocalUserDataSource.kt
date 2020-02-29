package com.jdevs.timeo.data.settings

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

interface LocalUserDataSource : UserDataSource {

    fun getPreferenceEnabled(name: String, defValue: Boolean): Boolean
}

@Singleton
class DefaultLocalUserDataSource @Inject constructor(context: Context) : LocalUserDataSource {

    private val sharedPrefs by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    override fun getPreferenceEnabled(name: String, defValue: Boolean) =
        sharedPrefs.getBoolean(name, defValue)

    override fun setPreferenceEnabled(name: String, isEnabled: Boolean) =
        sharedPrefs.edit { putBoolean(name, isEnabled) }
}
