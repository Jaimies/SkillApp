package com.jdevs.timeo.data

import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Task
import com.jdevs.timeo.domain.repository.TasksRepository
import com.jdevs.timeo.util.ListDataSource
import com.jdevs.timeo.util.createLiveData
import javax.inject.Inject

class FakeTasksRepository @Inject constructor() : TasksRepository {

    private val taskList = mutableListOf<Task>()
    override val tasks = ListDataSource.Factory(taskList)

    override fun getRemoteTasks(fetchNewItems: Boolean) = listOf(createLiveData<Operation<Task>>())
    override fun getTopTasks() = MutableLiveData(taskList.toList())

    override suspend fun addTask(name: String, projectId: String) {
        taskList.add(Task(taskList.lastIndex.toString(), name, projectId, 0, false))
    }

    override suspend fun deleteTask(task: Task) {
        taskList.removeAll { it.id == task.id }
    }

    override suspend fun setTaskCompleted(taskId: String, isCompleted: Boolean) {
        val index = taskList.indexOfFirst { it.id == taskId }
        taskList[index] = taskList[index].copy(isCompleted = isCompleted)
    }
}
