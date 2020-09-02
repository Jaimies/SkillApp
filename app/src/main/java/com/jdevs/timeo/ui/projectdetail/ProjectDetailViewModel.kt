package com.jdevs.timeo.ui.projectdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.jdevs.timeo.domain.usecase.projects.GetProjectByIdUseCase
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.util.time.getAvgWeekHours
import com.jdevs.timeo.util.time.getDaysSpentSince
import com.jdevs.timeo.util.time.getFriendlyHours
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProjectDetailViewModel(
    getProjectById: GetProjectByIdUseCase,
    projectId: Int
) : ViewModel() {
    val project =
        getProjectById.run(projectId).map { it.mapToPresentation() }.asLiveData()

    val state = project.map { ProjectDetailState(it) }

    class ProjectDetailState(project: ProjectItem) {
        val name = project.name
        val description = project.description
        val daysSpent = project.creationDate.getDaysSpentSince().toString()
        val avgWeekTime = getFriendlyHours(project.totalTime)
        val lastWeekTime =
            getAvgWeekHours(project.totalTime, project.creationDate)
        val totalTime = getFriendlyHours(project.lastWeekTime)
    }

    class Factory @Inject constructor(private val getProjectById: GetProjectByIdUseCase) {
        fun create(projectId: Int) =
            ProjectDetailViewModel(getProjectById, projectId)
    }
}
