package com.jdevs.timeo.ui.activities

import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.activities.GetActivitiesUseCase
import com.jdevs.timeo.domain.usecase.records.AddRecordUseCase
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import com.jdevs.timeo.util.mapTo
import com.jdevs.timeo.util.toLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ACTIVITIES_PAGE_SIZE = 20

class ActivitiesViewModel @Inject constructor(
    private val getActivities: GetActivitiesUseCase,
    private val addRecord: AddRecordUseCase
) : ListViewModel<ActivityItem>() {

    override val localLiveData
        get() =
            getActivities.activities.toLiveData(ACTIVITIES_PAGE_SIZE, Activity::mapToPresentation)

    override val remoteLiveDatas get() = getActivities.activitiesRemote.mapTo(Activity::mapToPresentation)
    val navigateToAddEdit = SingleLiveEvent<Any>()

    fun createRecord(activity: ActivityItem, time: Long) = viewModelScope.launch {

        val record = Record(name = activity.name, time = time, activityId = activity.id)
        addRecord(record)
    }

    fun navigateToAddActivity() = navigateToAddEdit.call()
}
