package com.jdevs.timeo.ui.summary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.domain.activities.GetTopActivitiesUseCase
import com.jdevs.timeo.domain.projects.GetTopProjectsUseCase
import com.jdevs.timeo.util.SingleLiveEvent
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
    getTopProjectsUseCase: GetTopProjectsUseCase,
    getTopActivitiesUseCase: GetTopActivitiesUseCase
) : ViewModel() {

    val topProjects = getTopProjectsUseCase.getTopProjects()
    val topActivities = getTopActivitiesUseCase.getTopActivities()

    val areProjectsEmpty get() = _areProjectsEmpty as LiveData<Boolean>
    val areActivitiesEmpty get() = _areActivitiesEmpty as LiveData<Boolean>
    val navigateToProjects = SingleLiveEvent<Any>()
    val navigateToActivities = SingleLiveEvent<Any>()

    private val _areProjectsEmpty = MutableLiveData(false)
    private val _areActivitiesEmpty = MutableLiveData(false)

    fun setProjectsSize(size: Int) {

        _areProjectsEmpty.value = size <= 0
    }

    fun setActivitiesSize(size: Int) {

        _areActivitiesEmpty.value = size <= 0
    }

    fun navigateToProjects() = navigateToProjects.call()
    fun navigateToActivities() = navigateToActivities.call()
}
