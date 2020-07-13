package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.settings.DefaultFirestoreUserDataSource
import com.jdevs.timeo.data.settings.DefaultLocalUserDataSource
import com.jdevs.timeo.data.settings.DefaultSettingsRepository
import com.jdevs.timeo.data.settings.FirestoreUserDataSource
import com.jdevs.timeo.data.settings.LocalUserDataSource
import com.jdevs.timeo.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface SettingsModule {
    @Binds
    fun provideRepository(settingsRepository: DefaultSettingsRepository): SettingsRepository

    @Binds
    fun provideRemoteDataSource(dataSource: DefaultFirestoreUserDataSource): FirestoreUserDataSource

    @Binds
    fun provideLocalDataSource(dataSource: DefaultLocalUserDataSource): LocalUserDataSource
}
