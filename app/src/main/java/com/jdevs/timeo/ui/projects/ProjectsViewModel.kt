package com.jdevs.timeo.ui.projects

import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.usecase.projects.GetProjectsUseCase
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import com.jdevs.timeo.util.mapTo
import com.jdevs.timeo.util.toLiveData
import javax.inject.Inject

private const val PROJECTS_PAGE_SIZE = 20

class ProjectsViewModel @Inject constructor(private val getProjects: GetProjectsUseCase) :
    ListViewModel<ProjectItem>() {

    override val localLiveData
        get() = getProjects.projects.toLiveData(PROJECTS_PAGE_SIZE, Project::mapToPresentation)

    override val remoteLiveDatas get() = getProjects.projectsRemote.mapTo(Project::mapToPresentation)

    val navigateToAddActivity = SingleLiveEvent<Any>()
    fun navigateToAddActivity() = navigateToAddActivity.call()
}
