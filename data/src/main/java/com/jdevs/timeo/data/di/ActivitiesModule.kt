package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.activities.ActivitiesDataSource
import com.jdevs.timeo.data.activities.DefaultActivitiesRepository
import com.jdevs.timeo.data.activities.RoomActivitiesDataSource
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface ActivitiesModule {
    @Binds
    fun provideRepository(repository: DefaultActivitiesRepository): ActivitiesRepository

    @Binds
    fun provideLocalDataSource(source: RoomActivitiesDataSource): ActivitiesDataSource
}
