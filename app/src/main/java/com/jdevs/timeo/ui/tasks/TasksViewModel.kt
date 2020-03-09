package com.jdevs.timeo.ui.tasks

import androidx.paging.toLiveData
import com.jdevs.timeo.domain.model.Task
import com.jdevs.timeo.domain.usecase.tasks.GetTasksUseCase
import com.jdevs.timeo.domain.usecase.tasks.SetTaskCompletedUseCase
import com.jdevs.timeo.model.TaskItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import com.jdevs.timeo.util.lifecycle.mapOperation
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val getTasks: GetTasksUseCase,
    private val setTaskCompleted: SetTaskCompletedUseCase
) : ListViewModel<TaskItem>() {

    override val localLiveData = getTasks.tasks.map(Task::mapToPresentation).toLiveData(PAGE_SIZE)

    override fun getRemoteLiveDatas(fetchNewItems: Boolean) =
        getTasks.getRemoteTasks(fetchNewItems).mapOperation(Task::mapToPresentation)

    fun setTaskCompleted(taskId: String, isCompleted: Boolean) = launchCoroutine {

        setTaskCompleted.invoke(taskId, isCompleted)
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}
