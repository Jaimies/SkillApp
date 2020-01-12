package com.jdevs.timeo.ui.projects

import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.domain.projects.GetProjectsUseCase
import com.jdevs.timeo.domain.settings.GetSettingsUseCase
import com.jdevs.timeo.util.SingleLiveEvent
import javax.inject.Inject

class ProjectsViewModel @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase
) : ListViewModel() {

    init {

        getProjectsUseCase.resetMonitor()
    }

    val activitiesEnabled get() = getSettingsUseCase.activitiesEnabled.value!!
    override val liveData get() = getProjectsUseCase.projects

    val navigateToAddActivity = SingleLiveEvent<Any>()
    fun navigateToAddActivity() = navigateToAddActivity.call()
}
