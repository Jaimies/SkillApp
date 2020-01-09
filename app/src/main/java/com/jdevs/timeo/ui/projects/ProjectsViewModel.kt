package com.jdevs.timeo.ui.projects

import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.domain.projects.GetProjectsUseCase
import com.jdevs.timeo.util.SingleLiveEvent
import javax.inject.Inject

class ProjectsViewModel @Inject constructor(private val getProjectsUseCase: GetProjectsUseCase) :
    ListViewModel() {

    init {

        getProjectsUseCase.resetMonitor()
    }

    override val liveData get() = getProjectsUseCase.projects

    val navigateToAddActivity = SingleLiveEvent<Any>()
    fun navigateToAddActivity() = navigateToAddActivity.call()
}
