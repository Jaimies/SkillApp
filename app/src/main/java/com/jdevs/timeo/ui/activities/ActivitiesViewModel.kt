package com.jdevs.timeo.ui.activities

import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.usecases.GetActivitiesUseCase
import com.jdevs.timeo.util.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivitiesViewModel @Inject constructor(
    private val getActivitiesUseCase: GetActivitiesUseCase
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

        getActivitiesUseCase.addRecord(record)
    }

    fun navigateToAddActivity() = navigateToAddEdit.call()
}
