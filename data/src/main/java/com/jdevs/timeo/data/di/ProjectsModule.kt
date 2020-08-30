package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.projects.DefaultProjectsRepository
import com.jdevs.timeo.data.projects.ProjectsLocalDataSource
import com.jdevs.timeo.data.projects.RoomProjectsDataSource
import com.jdevs.timeo.domain.repository.ProjectsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface ProjectsModule {
    @Binds
    fun provideRepository(repository: DefaultProjectsRepository): ProjectsRepository

    @Binds
    fun provideLocalDataSource(source: RoomProjectsDataSource): ProjectsLocalDataSource
}
