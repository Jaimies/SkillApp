package com.jdevs.timeo.ui.activities

import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.activities.GetActivitiesUseCase
import com.jdevs.timeo.domain.usecase.records.AddRecordUseCase
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivitiesViewModel @Inject constructor(
    private val getActivities: GetActivitiesUseCase,
    private val addRecord: AddRecordUseCase
) : ListViewModel() {

    override val liveData get() = getActivities()
    val navigateToAddEdit = SingleLiveEvent<Any>()

    init {

        getActivities.resetActivities()
    }

    fun createRecord(activity: Activity, time: Long) = viewModelScope.launch {

        val record = Record(
            name = activity.name,
            time = time,
            activityId = activity.documentId,
            roomActivityId = activity.id
        )

        addRecord(record)
    }

    fun navigateToAddActivity() = navigateToAddEdit.call()
}
