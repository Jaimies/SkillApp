package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.activities.ActivitiesRepositoryImpl
import com.jdevs.timeo.data.records.RecordsRepositoryImpl
import com.jdevs.timeo.data.stats.StatsRepositoryImpl
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import com.jdevs.timeo.domain.repository.RecordsRepository
import com.jdevs.timeo.domain.repository.StatsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface RepositoryModule {
    @Binds
    fun provideActivitiesRepository(repository: ActivitiesRepositoryImpl): ActivitiesRepository

    @Binds
    fun provideRecordsRepository(repository: RecordsRepositoryImpl): RecordsRepository

    @Binds
    fun provideStatsRepository(repository: StatsRepositoryImpl): StatsRepository
}
