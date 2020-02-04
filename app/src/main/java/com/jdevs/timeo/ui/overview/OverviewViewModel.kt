package com.jdevs.timeo.ui.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.domain.usecase.activities.GetTopActivitiesUseCase
import com.jdevs.timeo.domain.usecase.projects.GetTopProjectsUseCase
import com.jdevs.timeo.domain.usecase.settings.GetSettingsUseCase
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import javax.inject.Inject

class OverviewViewModel @Inject constructor(
    private val getTopProjects: GetTopProjectsUseCase,
    private val getTopActivities: GetTopActivitiesUseCase,
    private val settings: GetSettingsUseCase
) : ViewModel() {

    val topProjects get() = getTopProjects()
    val topActivities get() = getTopActivities()
    val activitiesEnabled get() = settings.activitiesEnabled

    val areProjectsLoading get() = _areProjectsLoading as LiveData<Boolean>
    val areProjectsEmpty get() = _areProjectsEmpty as LiveData<Boolean>
    val areActivitiesLoading get() = _areActivitiesLoading as LiveData<Boolean>
    val areActivitiesEmpty get() = _areActivitiesEmpty as LiveData<Boolean>
    private val _areProjectsLoading = MutableLiveData(true)
    private val _areProjectsEmpty = MutableLiveData(false)
    private val _areActivitiesLoading = MutableLiveData(true)
    private val _areActivitiesEmpty = MutableLiveData(false)

    val navigateToProjects = SingleLiveEvent<Any>()
    val navigateToActivities = SingleLiveEvent<Any>()

    fun setProjectsSize(size: Int) {

        _areProjectsLoading.value = false
        _areProjectsEmpty.value = size <= 0
    }

    fun setActivitiesSize(size: Int) {

        _areActivitiesLoading.value = false
        _areActivitiesEmpty.value = size <= 0
    }

    fun navigateToProjects() = navigateToProjects.call()
    fun navigateToActivities() = navigateToActivities.call()
}
