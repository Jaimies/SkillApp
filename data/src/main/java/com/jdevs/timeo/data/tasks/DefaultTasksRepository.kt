package com.jdevs.timeo.data.tasks

import com.jdevs.timeo.data.Repository
import com.jdevs.timeo.domain.model.Task
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.domain.repository.TasksRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultTasksRepository @Inject constructor(
    private val localDataSource: TasksLocalDataSource,
    private val remoteDataSource: TasksRemoteDataSource,
    authRepository: AuthRepository
) :
    Repository<TasksDataSource>(remoteDataSource, localDataSource, authRepository),
    TasksRepository {

    override val tasks get() = localDataSource.tasks

    override fun getRemoteTasks(fetchNewItems: Boolean) = remoteDataSource.getTasks(fetchNewItems)

    override fun getTopTasks() = currentDataSource.getTopTasks()

    override suspend fun addTask(name: String, projectId: String) =
        currentDataSource.addTask(name, projectId)

    override suspend fun deleteTask(task: Task) = currentDataSource.deleteTask(task)

    override suspend fun setTaskCompleted(taskId: String, isCompleted: Boolean) {

        currentDataSource.setTaskCompleted(taskId, isCompleted)
    }
}