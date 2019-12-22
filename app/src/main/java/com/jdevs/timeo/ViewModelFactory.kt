package com.jdevs.timeo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jdevs.timeo.data.source.ActivitiesRepository
import com.jdevs.timeo.ui.activities.viewmodel.ActivityListViewModel
import com.jdevs.timeo.ui.activities.viewmodel.AddEditActivityViewModel

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val tasksRepository: ActivitiesRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ActivityListViewModel::class.java) ->
                    ActivityListViewModel(tasksRepository)
                isAssignableFrom(AddEditActivityViewModel::class.java) ->
                    AddEditActivityViewModel(tasksRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
