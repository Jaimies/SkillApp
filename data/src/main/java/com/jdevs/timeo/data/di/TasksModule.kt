package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.tasks.DefaultTasksRepository
import com.jdevs.timeo.data.tasks.FirestoreTasksDataSource
import com.jdevs.timeo.data.tasks.RoomTasksDataSource
import com.jdevs.timeo.data.tasks.TasksLocalDataSource
import com.jdevs.timeo.data.tasks.TasksRemoteDataSource
import com.jdevs.timeo.domain.repository.TasksRepository
import dagger.Binds
import dagger.Module

@Module
interface TasksModule {

    @Binds
    fun provideRepository(tasksRepository: DefaultTasksRepository): TasksRepository

    @Binds
    fun provideLocalDataSource(localDataSource: RoomTasksDataSource): TasksLocalDataSource

    @Binds
    fun provideRemoteDataSource(remoteDataSource: FirestoreTasksDataSource): TasksRemoteDataSource
}
