package com.jdevs.timeo.ui.tasks

import androidx.paging.toLiveData
import com.jdevs.timeo.domain.model.Task
import com.jdevs.timeo.domain.usecase.tasks.GetTasksUseCase
import com.jdevs.timeo.model.TaskItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.mapTo
import javax.inject.Inject

class TasksViewModel @Inject constructor(private val getTasks: GetTasksUseCase) :
    ListViewModel<TaskItem>() {

    override val localLiveData
        get() = getTasks.tasks.map(Task::mapToPresentation).toLiveData(PAGE_SIZE)

    override fun getRemoteLiveDatas(fetchNewItems: Boolean) =
        getTasks.getRemoteTasks(fetchNewItems).mapTo(Task::mapToPresentation)

    companion object {
        private const val PAGE_SIZE = 30
    }
}
