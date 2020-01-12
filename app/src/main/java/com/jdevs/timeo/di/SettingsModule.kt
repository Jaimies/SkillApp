package com.jdevs.timeo.di

import android.content.Context
import android.content.SharedPreferences
import com.jdevs.timeo.data.settings.DefaultSettingsRepository
import com.jdevs.timeo.data.settings.FirestoreUserDataSource
import com.jdevs.timeo.data.settings.SettingsRepository
import com.jdevs.timeo.data.settings.UserDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class SettingsModule {

    @Binds
    abstract fun provideSettingsRepository(settingsRepository: DefaultSettingsRepository): SettingsRepository

    @Binds
    abstract fun provideUserDataSource(dataSource: FirestoreUserDataSource): UserDataSource

    @Module
    companion object {

        private const val SETTINGS_PREF = "com.jdevs.timeo.settings"

        @Provides
        @Singleton
        @JvmStatic
        fun provideSharedPrefs(context: Context): SharedPreferences =
            context.getSharedPreferences(SETTINGS_PREF, Context.MODE_PRIVATE)
    }
}
