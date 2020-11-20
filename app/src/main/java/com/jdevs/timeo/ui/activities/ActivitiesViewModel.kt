package com.jdevs.timeo.ui.activities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.paging.toLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.activities.GetActivitiesUseCase
import com.jdevs.timeo.domain.usecase.records.AddRecordUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.util.lifecycle.launchCoroutine

const val ACTIVITIES_PAGE_SIZE = 20

class ActivitiesViewModel @ViewModelInject constructor(
    getActivities: GetActivitiesUseCase,
    private val addRecord: AddRecordUseCase
) : ViewModel() {

    val activities =
        getActivities.run().map(Activity::mapToPresentation)
            .toLiveData(ACTIVITIES_PAGE_SIZE)

    val navigateToAddEdit = SingleLiveEvent<Any>()

    fun createRecord(activity: ActivityItem, time: Int) = launchCoroutine {
        val record =
            Record(name = activity.name, time = time, activityId = activity.id)
        addRecord.run(record)
    }

    fun navigateToAddActivity() = navigateToAddEdit.call()
}
