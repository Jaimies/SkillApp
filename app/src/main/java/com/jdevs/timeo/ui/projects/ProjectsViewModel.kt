package com.jdevs.timeo.ui.projects

import androidx.hilt.lifecycle.ViewModelInject
import androidx.paging.toLiveData
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.usecase.projects.GetProjectsUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel

private const val PROJECTS_PAGE_SIZE = 20

class ProjectsViewModel @ViewModelInject constructor(
    getProjects: GetProjectsUseCase
) : ListViewModel<ProjectItem>() {

    override val liveData = getProjects.run()
        .map(Project::mapToPresentation)
        .toLiveData(PROJECTS_PAGE_SIZE)

    val navigateToAddActivity = SingleLiveEvent<Any>()
    fun navigateToAddActivity() = navigateToAddActivity.call()
}
