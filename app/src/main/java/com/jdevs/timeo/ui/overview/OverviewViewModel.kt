package com.jdevs.timeo.ui.overview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.activities.GetTopActivitiesUseCase
import com.jdevs.timeo.domain.usecase.records.AddRecordUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.shared.util.mapList
import com.jdevs.timeo.util.lifecycle.launchCoroutine

class OverviewViewModel @ViewModelInject constructor(
    getTopActivities: GetTopActivitiesUseCase,
    private val addRecord: AddRecordUseCase
) : ViewModel() {

    val activities = DataWrapper(
        getTopActivities.run().mapList { it.mapToPresentation() }.asLiveData()
    )

    fun createRecord(index: Int, time: Int) = launchCoroutine {
        val activity = activities.data.value!![index]
        val record =
            Record(name = activity.name, time = time, activityId = activity.id)
        addRecord.run(record)
    }

    class DataWrapper<T : ViewItem>(val data: LiveData<List<T>>) {
        val isEmpty = data.map { it.isEmpty() }
        val navigateToList = SingleLiveEvent<Any>()
        val navigateToAdd = SingleLiveEvent<Any>()

        fun navigateToList() = navigateToList.call()
        fun navigateToAdd() = navigateToAdd.call()
    }
}
