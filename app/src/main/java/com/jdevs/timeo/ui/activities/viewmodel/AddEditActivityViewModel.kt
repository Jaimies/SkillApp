package com.jdevs.timeo.ui.activities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.common.viewmodel.KeyboardHidingViewModel
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.util.SingleLiveEvent
import kotlinx.coroutines.launch

class AddEditActivityViewModel(private val repository: TimeoRepository) :
    KeyboardHidingViewModel() {

    val name = MutableLiveData("")
    val nameError get() = _nameError as LiveData<String>
    val activityExists get() = _activityExists as LiveData<Boolean>
    val showDeleteDialog = SingleLiveEvent<Any>()
    val saveActivity = SingleLiveEvent<String>()

    private val _nameError = MutableLiveData("")
    private val _activityExists = MutableLiveData(false)

    fun setActivity(activity: Activity?) {

        name.value = activity?.name.orEmpty()
        _activityExists.value = true
    }

    fun setNameError(error: String) {

        _nameError.value = error
    }

    fun triggerSaveActivity() {

        saveActivity.value = name.value.orEmpty()
    }

    fun addActivity(activity: Activity) = viewModelScope.launch {

        repository.addActivity(activity)
    }

    fun saveActivity(activity: Activity) = viewModelScope.launch {

        repository.saveActivity(activity)
    }

    fun deleteActivity(activity: Activity) = viewModelScope.launch {

        repository.deleteActivity(activity)
    }

    fun showDeleteDialog() = showDeleteDialog.call()
}
