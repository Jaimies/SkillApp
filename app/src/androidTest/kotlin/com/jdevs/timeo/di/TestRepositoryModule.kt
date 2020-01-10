package com.jdevs.timeo.di

import com.jdevs.timeo.data.FakeActivitiesRepository
import com.jdevs.timeo.data.FakeProjectsRepository
import com.jdevs.timeo.data.FakeRecordsRepository
import com.jdevs.timeo.data.FakeStatsRepository
import com.jdevs.timeo.data.activities.ActivitiesRepository
import com.jdevs.timeo.data.projects.ProjectsRepository
import com.jdevs.timeo.data.records.RecordsRepository
import com.jdevs.timeo.data.stats.StatsRepository
import dagger.Binds
import dagger.Module

@Module
interface TestRepositoryModule {

    @Binds
    fun provideActivitiesRepository(repository: FakeActivitiesRepository): ActivitiesRepository

    @Binds
    fun provideProjectsRepository(repository: FakeProjectsRepository): ProjectsRepository

    @Binds
    fun provideRecordsRepository(repository: FakeRecordsRepository): RecordsRepository

    @Binds
    fun provideStatsRepository(repository: FakeStatsRepository): StatsRepository
}
