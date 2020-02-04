package com.jdevs.timeo.ui.projects

import com.jdevs.timeo.domain.usecase.projects.GetProjectsUseCase
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import javax.inject.Inject

class ProjectsViewModel @Inject constructor(private val getProjects: GetProjectsUseCase) :
    ListViewModel() {

    override val localLiveData get() = getProjects.projects
    override val remoteLiveDatas get() = getProjects.projectsRemote

    val navigateToAddActivity = SingleLiveEvent<Any>()
    fun navigateToAddActivity() = navigateToAddActivity.call()
}
