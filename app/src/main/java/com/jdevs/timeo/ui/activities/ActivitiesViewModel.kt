package com.jdevs.timeo.ui.activities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.activities.GetActivitiesUseCase
import com.jdevs.timeo.domain.usecase.records.AddRecordUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.shared.util.mapList
import com.jdevs.timeo.util.lifecycle.launchCoroutine

class ActivitiesViewModel @ViewModelInject constructor(
    getActivities: GetActivitiesUseCase,
    private val addRecord: AddRecordUseCase
) : ViewModel() {

    val activities = getActivities.run()
        .mapList { it.mapToPresentation() }
        .asLiveData()

    val isEmpty = activities.map { it.isEmpty() }

    val navigateToAddEdit = SingleLiveEvent<Any>()

    fun createRecord(activity: ActivityItem, time: Int) = launchCoroutine {
        val record = Record(activity.name, activity.id, time)
        addRecord.run(record)
    }

    fun navigateToAddActivity() = navigateToAddEdit.call()
}
