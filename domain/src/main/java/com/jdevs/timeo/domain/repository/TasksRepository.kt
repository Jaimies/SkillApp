package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Task

interface TasksRepository {

    val tasks: DataSource.Factory<Int, Task>
    fun getRemoteTasks(fetchNewItems: Boolean): List<LiveData<Operation<Task>>>

    fun getTopTasks(): LiveData<List<Task>>

    suspend fun addTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun setTaskCompleted(taskId: String, isCompleted: Boolean)
}
