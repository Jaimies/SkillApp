package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.projects.DefaultProjectsRepository
import com.jdevs.timeo.data.projects.FirestoreProjectsDataSource
import com.jdevs.timeo.data.projects.ProjectsLocalDataSource
import com.jdevs.timeo.data.projects.ProjectsRemoteDataSource
import com.jdevs.timeo.data.projects.RoomProjectsDataSource
import com.jdevs.timeo.domain.repository.ProjectsRepository
import dagger.Binds
import dagger.Module

@Module
interface ProjectsModule {

    @Binds
    fun provideRepository(repository: DefaultProjectsRepository): ProjectsRepository

    @Binds
    fun provideRemoteDataSource(source: FirestoreProjectsDataSource): ProjectsRemoteDataSource

    @Binds
    fun provideLocalDataSource(source: RoomProjectsDataSource): ProjectsLocalDataSource
}
