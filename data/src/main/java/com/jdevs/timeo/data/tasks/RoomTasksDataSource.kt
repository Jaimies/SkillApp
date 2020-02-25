package com.jdevs.timeo.data.tasks

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Task
import com.jdevs.timeo.shared.util.mapList
import javax.inject.Inject

interface TasksDataSource {

    fun getTopTasks(): LiveData<List<Task>>

    suspend fun addTask(name: String, projectId: String)

    suspend fun deleteTask(task: Task)

    suspend fun setTaskCompleted(taskId: String, isCompleted: Boolean)
}

interface TasksLocalDataSource : TasksDataSource {

    val tasks: DataSource.Factory<Int, Task>
}

class RoomTasksDataSource @Inject constructor(private val tasksDao: TasksDao) :
    TasksLocalDataSource {

    override val tasks by lazy { tasksDao.getTasks().map(DBTask::mapToDomain) }

    override fun getTopTasks() = tasksDao.getTopTasks().mapList(DBTask::mapToDomain)

    override suspend fun addTask(name: String, projectId: String) {

        tasksDao.insert(DBTask(name = name, projectId = projectId.toInt()))
    }

    override suspend fun deleteTask(task: Task) = tasksDao.delete(task.mapToDB())

    override suspend fun setTaskCompleted(taskId: String, isCompleted: Boolean) {

        tasksDao.setTaskCompleted(taskId.toInt(), isCompleted)
    }
}
