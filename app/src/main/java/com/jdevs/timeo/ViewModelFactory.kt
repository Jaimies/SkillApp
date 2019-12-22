package com.jdevs.timeo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.ui.activities.viewmodel.ActivityListViewModel
import com.jdevs.timeo.ui.activities.viewmodel.AddEditActivityViewModel
import com.jdevs.timeo.ui.activities.viewmodel.HistoryViewModel

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: TimeoRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ActivityListViewModel::class.java) ->
                    ActivityListViewModel(repository)
                isAssignableFrom(AddEditActivityViewModel::class.java) ->
                    AddEditActivityViewModel(repository)
                isAssignableFrom(HistoryViewModel::class.java) ->
                    HistoryViewModel(repository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
