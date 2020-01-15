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
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val addRecordUseCase: AddRecordUseCase
) : ListViewModel() {

    override val liveData get() = getActivitiesUseCase.activities
    val navigateToAddEdit = SingleLiveEvent<Any>()

    init {

        getActivitiesUseCase.resetActivities()
    }

    fun createRecord(activity: Activity, time: Long) = viewModelScope.launch {

        val record = Record(
            name = activity.name,
            time = time,
            activityId = activity.documentId,
            roomActivityId = activity.id
        )

        addRecordUseCase.addRecord(record)
    }

    fun navigateToAddActivity() = navigateToAddEdit.call()
}
