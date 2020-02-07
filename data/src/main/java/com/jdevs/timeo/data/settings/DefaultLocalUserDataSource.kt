package com.jdevs.timeo.data.settings

import android.content.SharedPreferences
import javax.inject.Inject

interface LocalUserDataSource : UserDataSource {

    fun getPreferenceEnabled(name: String, defValue: Boolean): Boolean
}

class DefaultLocalUserDataSource @Inject constructor(private val sharedPrefs: SharedPreferences) :
    LocalUserDataSource {

    override fun getPreferenceEnabled(name: String, defValue: Boolean) =
        sharedPrefs.getBoolean(name, defValue)

    override fun enablePreference(name: String, isEnabled: Boolean) =
        sharedPrefs.edit().putBoolean(name, isEnabled).apply()
}
