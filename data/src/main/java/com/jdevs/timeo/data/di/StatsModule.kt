package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.stats.StatsRepositoryImpl
import com.jdevs.timeo.domain.repository.StatsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface StatsModule {
    @Binds
    fun provideRepository(repository: StatsRepositoryImpl): StatsRepository
}
