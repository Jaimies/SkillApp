package com.jdevs.timeo.ui.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.activities.GetTopActivitiesUseCase
import com.jdevs.timeo.domain.usecase.projects.GetTopProjectsUseCase
import com.jdevs.timeo.domain.usecase.records.AddRecordUseCase
import com.jdevs.timeo.domain.usecase.settings.GetSettingsUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.shared.util.mapList
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import javax.inject.Inject

class OverviewViewModel @Inject constructor(
    getTopProjects: GetTopProjectsUseCase,
    getTopActivities: GetTopActivitiesUseCase,
    private val addRecord: AddRecordUseCase,
    private val settings: GetSettingsUseCase
) : ViewModel() {

    val activitiesEnabled get() = settings.activitiesEnabled

    val activities = DataHolder { getTopActivities().mapList(Activity::mapToPresentation) }
    val projects = DataHolder { getTopProjects().mapList(Project::mapToPresentation) }

    fun createRecord(index: Int, time: Int) = launchCoroutine {

        val activity = activities.data.value!![index]
        val record = Record(name = activity.name, time = time, activityId = activity.id)
        addRecord(record)
    }

    class DataHolder<T : ViewItem>(initializeData: () -> LiveData<List<T>>) {

        val data by lazy(initializeData)
        val isLoading get() = _isLoading as LiveData<Boolean>
        val isEmpty get() = _isEmpty as LiveData<Boolean>
        private val _isLoading = MutableLiveData(true)
        private val _isEmpty = MutableLiveData(false)

        val navigateToList = SingleLiveEvent<Any>()
        val navigateToAdd = SingleLiveEvent<Any>()

        fun setSize(size: Int) {

            _isLoading.value = false
            _isEmpty.value = size <= 0
        }

        fun navigateToList() = navigateToList.call()
        fun navigateToAdd() = navigateToAdd.call()
    }
}
