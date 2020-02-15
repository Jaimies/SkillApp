package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.stats.DefaultStatsRepository
import com.jdevs.timeo.data.stats.FirestoreStatsDataSource
import com.jdevs.timeo.data.stats.RoomStatsDataSource
import com.jdevs.timeo.data.stats.StatsDataSource
import com.jdevs.timeo.data.stats.StatsRemoteDataSource
import com.jdevs.timeo.domain.repository.StatsRepository
import dagger.Binds
import dagger.Module

@Module
interface StatsModule {

    @Binds
    fun provideStatsRepository(repository: DefaultStatsRepository): StatsRepository

    @Binds
    fun provideStatsLocalDataSource(source: RoomStatsDataSource): StatsDataSource

    @Binds
    fun provideStatsRemoteDataSource(source: FirestoreStatsDataSource): StatsRemoteDataSource
}
