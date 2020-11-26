package com.maxpoliakov.skillapp.ui.activities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.activities.GetActivitiesUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.model.ActivityItem
import com.maxpoliakov.skillapp.model.mapToPresentation
import com.maxpoliakov.skillapp.shared.util.mapList
import com.maxpoliakov.skillapp.util.lifecycle.launchCoroutine
import java.time.Duration

class ActivitiesViewModel @ViewModelInject constructor(
    getActivities: GetActivitiesUseCase,
    private val addRecord: AddRecordUseCase
) : ViewModel() {

    val activities = getActivities.run()
        .mapList { it.mapToPresentation() }
        .asLiveData()

    val isEmpty = activities.map { it.isEmpty() }

    val navigateToAddEdit = SingleLiveEvent<Any>()

    fun createRecord(activity: ActivityItem, time: Duration) = launchCoroutine {
        val record = Record(activity.name, activity.id, time)
        addRecord.run(record)
    }

    fun navigateToAddActivity() = navigateToAddEdit.call()
}
