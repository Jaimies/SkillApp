package com.jdevs.timeo.di

import com.jdevs.timeo.data.settings.DefaultFirestoreUserDataSource
import com.jdevs.timeo.data.settings.DefaultLocalUserDataSource
import com.jdevs.timeo.data.settings.DefaultSettingsRepository
import com.jdevs.timeo.data.settings.FirestoreUserDataSource
import com.jdevs.timeo.data.settings.LocalUserDataSource
import com.jdevs.timeo.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module

@Module
interface SettingsModule {

    @Binds
    fun provideSettingsRepository(settingsRepository: DefaultSettingsRepository): SettingsRepository

    @Binds
    fun provideFirestoreUserDataSource(dataSource: DefaultFirestoreUserDataSource): FirestoreUserDataSource

    @Binds
    fun provideSharedPrefsUserDataSource(dataSource: DefaultLocalUserDataSource): LocalUserDataSource
}
