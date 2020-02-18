package com.jdevs.timeo.data.tasks

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Task
import com.jdevs.timeo.shared.util.map
import javax.inject.Inject

interface TasksDataSource {

    fun getTopTasks(): LiveData<List<Task>>

    suspend fun addTask(task: Task)

    suspend fun deleteTask(task: Task)
}

interface TasksLocalDataSource : TasksDataSource {

    val tasks: DataSource.Factory<Int, Task>
}

class RoomTasksDataSource @Inject constructor(private val tasksDao: TasksDao) :
    TasksLocalDataSource {

    override val tasks by lazy { tasksDao.getTasks().map(DBTask::mapToDomain) }

    override fun getTopTasks() = map(tasksDao.getTopTasks(), DBTask::mapToDomain)

    override suspend fun addTask(task: Task) = tasksDao.insert(task.mapToDB())

    override suspend fun deleteTask(task: Task) = tasksDao.delete(task.mapToDB())
}
