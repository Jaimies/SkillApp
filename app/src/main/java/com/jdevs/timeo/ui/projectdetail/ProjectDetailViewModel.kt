package com.jdevs.timeo.ui.projectdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.model.Task
import com.jdevs.timeo.domain.usecase.projects.GetProjectByIdUseCase
import com.jdevs.timeo.domain.usecase.tasks.GetTopTasksUseCase
import com.jdevs.timeo.domain.usecase.tasks.SetTaskCompletedUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.shared.util.mapList
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import com.jdevs.timeo.util.time.getAvgWeekHours
import com.jdevs.timeo.util.time.getDaysSpentSince
import com.jdevs.timeo.util.time.getFriendlyHours
import javax.inject.Inject

class ProjectDetailViewModel(
    private val setTaskCompleted: SetTaskCompletedUseCase,
    getProjectById: GetProjectByIdUseCase,
    getTopTasks: GetTopTasksUseCase,
    projectId: String
) : ViewModel() {

    val goToTasks = SingleLiveEvent<Any>()
    val addTask = SingleLiveEvent<Any>()

    val topTasks by lazy { getTopTasks().mapList(Task::mapToPresentation) }

    val project = getProjectById(projectId).map(Project::mapToPresentation)
    val state = project.map { ProjectDetailState(it) }

    class ProjectDetailState(project: ProjectItem) {
        val name = project.name
        val description = project.description
        val daysSpent = project.creationDate.getDaysSpentSince().toString()
        val avgWeekTime = getFriendlyHours(project.totalTime)
        val lastWeekTime = getAvgWeekHours(project.totalTime, project.creationDate)
        val totalTime = getFriendlyHours(project.lastWeekTime)
    }

    fun setTaskCompleted(position: Int, isCompleted: Boolean) = launchCoroutine {
        setTaskCompleted.invoke(topTasks.value!![position].id, isCompleted)
    }

    fun goToTasks() = goToTasks.call()
    fun addTask() = addTask.call()

    class Factory @Inject constructor(
        private val setTaskCompleted: SetTaskCompletedUseCase,
        private val getProjectById: GetProjectByIdUseCase,
        private val getTopTasks: GetTopTasksUseCase
    ) {
        fun create(projectId: String) = ProjectDetailViewModel(
            setTaskCompleted, getProjectById, getTopTasks, projectId
        )
    }
}
