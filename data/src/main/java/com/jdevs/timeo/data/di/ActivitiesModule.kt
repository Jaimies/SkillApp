package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.activities.ActivitiesLocalDataSource
import com.jdevs.timeo.data.activities.ActivitiesRemoteDataSource
import com.jdevs.timeo.data.activities.DefaultActivitiesRepository
import com.jdevs.timeo.data.activities.FirestoreActivitiesDataSource
import com.jdevs.timeo.data.activities.RoomActivitiesDataSource
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import dagger.Binds
import dagger.Module

@Module
interface ActivitiesModule {

    @Binds
    fun provideActivitiesRepository(repository: DefaultActivitiesRepository): ActivitiesRepository

    @Binds
    fun provideActivitiesRemoteDataSource(source: FirestoreActivitiesDataSource): ActivitiesRemoteDataSource

    @Binds
    fun provideActivitiesLocalDataSource(source: RoomActivitiesDataSource): ActivitiesLocalDataSource
}
