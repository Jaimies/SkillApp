package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.activities.ActivitiesRepositoryImpl
import com.maxpoliakov.skillapp.data.records.RecordsRepositoryImpl
import com.maxpoliakov.skillapp.data.stats.StatsRepositoryImpl
import com.maxpoliakov.skillapp.domain.repository.ActivitiesRepository
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
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
