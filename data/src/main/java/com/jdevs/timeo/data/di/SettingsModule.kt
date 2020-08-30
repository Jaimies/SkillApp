package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.settings.DefaultSettingsRepository
import com.jdevs.timeo.data.settings.DefaultUserDataSource
import com.jdevs.timeo.data.settings.UserDataSource
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
    fun provideLocalDataSource(dataSource: DefaultUserDataSource): UserDataSource
}
