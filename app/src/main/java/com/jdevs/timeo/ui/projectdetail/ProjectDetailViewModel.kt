package com.jdevs.timeo.ui.projectdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.usecase.projects.GetProjectByIdUseCase
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.util.time.getAvgWeekHours
import com.jdevs.timeo.util.time.getDaysSpentSince
import com.jdevs.timeo.util.time.getFriendlyHours
import javax.inject.Inject

class ProjectDetailViewModel(
    getProjectById: GetProjectByIdUseCase,
    projectId: String
) : ViewModel() {
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

    class Factory @Inject constructor(private val getProjectById: GetProjectByIdUseCase) {
        fun create(projectId: String) = ProjectDetailViewModel(getProjectById, projectId)
    }
}
