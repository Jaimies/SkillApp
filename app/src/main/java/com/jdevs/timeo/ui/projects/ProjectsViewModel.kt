package com.jdevs.timeo.ui.projects

import androidx.hilt.lifecycle.ViewModelInject
import androidx.paging.toLiveData
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.domain.usecase.projects.GetProjectsUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.lifecycle.mapOperation

private const val PROJECTS_PAGE_SIZE = 20

class ProjectsViewModel @ViewModelInject constructor(
    private val getProjects: GetProjectsUseCase,
    authRepository: AuthRepository
) : ListViewModel<ProjectItem>(authRepository) {

    override val localLiveData =
        getProjects.projects.map(Project::mapToPresentation).toLiveData(PROJECTS_PAGE_SIZE)

    override fun getRemoteLiveDatas(fetchNewItems: Boolean) =
        getProjects.getProjectsRemote(fetchNewItems).mapOperation(Project::mapToPresentation)

    val navigateToAddActivity = SingleLiveEvent<Any>()
    fun navigateToAddActivity() = navigateToAddActivity.call()
}
