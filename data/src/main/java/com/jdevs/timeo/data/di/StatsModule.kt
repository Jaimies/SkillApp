package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.stats.DefaultStatsRepository
import com.jdevs.timeo.data.stats.FirestoreStatsDataSource
import com.jdevs.timeo.data.stats.RoomStatsDataSource
import com.jdevs.timeo.data.stats.StatsDataSource
import com.jdevs.timeo.data.stats.StatsRemoteDataSource
import com.jdevs.timeo.domain.repository.StatsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface StatsModule {
    @Binds
    fun provideRepository(repository: DefaultStatsRepository): StatsRepository

    @Binds
    fun provideLocalDataSource(source: RoomStatsDataSource): StatsDataSource

    @Binds
    fun provideRemoteDataSource(source: FirestoreStatsDataSource): StatsRemoteDataSource
}
