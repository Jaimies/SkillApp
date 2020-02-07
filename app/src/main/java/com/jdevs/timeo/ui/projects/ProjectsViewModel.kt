package com.jdevs.timeo.ui.projects

import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.usecase.projects.GetProjectsUseCase
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.ui.model.ProjectItem
import com.jdevs.timeo.ui.model.mapToPresentation
import com.jdevs.timeo.util.PagingConstants.PROJECTS_PAGE_SIZE
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import com.jdevs.timeo.util.mapTo
import com.jdevs.timeo.util.toPagedList
import javax.inject.Inject

class ProjectsViewModel @Inject constructor(private val getProjects: GetProjectsUseCase) :
    ListViewModel<ProjectItem>() {

    override val localLiveData
        get() = getProjects.projects.toPagedList(PROJECTS_PAGE_SIZE, Project::mapToPresentation)

    override val remoteLiveDatas get() = getProjects.projectsRemote.mapTo(Project::mapToPresentation)

    val navigateToAddActivity = SingleLiveEvent<Any>()
    fun navigateToAddActivity() = navigateToAddActivity.call()
}
