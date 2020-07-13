package com.jdevs.timeo.ui.activities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.paging.toLiveData
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.activities.GetActivitiesUseCase
import com.jdevs.timeo.domain.usecase.records.AddRecordUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import com.jdevs.timeo.util.lifecycle.mapOperation

private const val ACTIVITIES_PAGE_SIZE = 20

class ActivitiesViewModel @ViewModelInject constructor(
    private val getActivities: GetActivitiesUseCase,
    private val addRecord: AddRecordUseCase
) : ListViewModel<ActivityItem>() {

    override val localLiveData =
        getActivities.activities.map(Activity::mapToPresentation).toLiveData(ACTIVITIES_PAGE_SIZE)

    override fun getRemoteLiveDatas(fetchNewItems: Boolean) =
        getActivities.getActivitiesRemote(fetchNewItems).mapOperation(Activity::mapToPresentation)

    val navigateToAddEdit = SingleLiveEvent<Any>()

    fun createRecord(activity: ActivityItem, time: Int) = launchCoroutine {

        val record = Record(name = activity.name, time = time, activityId = activity.id)
        addRecord(record)
    }

    fun navigateToAddActivity() = navigateToAddEdit.call()
}
