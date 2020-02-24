package com.jdevs.timeo.di

import com.jdevs.timeo.data.FakeActivitiesRepository
import com.jdevs.timeo.data.FakeProjectsRepository
import com.jdevs.timeo.data.FakeRecordsRepository
import com.jdevs.timeo.data.FakeSettingsRepository
import com.jdevs.timeo.data.FakeStatsRepository
import com.jdevs.timeo.data.FakeTasksRepository
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import com.jdevs.timeo.domain.repository.ProjectsRepository
import com.jdevs.timeo.domain.repository.RecordsRepository
import com.jdevs.timeo.domain.repository.SettingsRepository
import com.jdevs.timeo.domain.repository.StatsRepository
import com.jdevs.timeo.domain.repository.TasksRepository
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
    fun provideTasksRepository(repository: FakeTasksRepository): TasksRepository

    @Binds
    fun provideStatsRepository(repository: FakeStatsRepository): StatsRepository

    @Binds
    fun provideSettingsRepository(repository: FakeSettingsRepository): SettingsRepository
}
