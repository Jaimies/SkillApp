package com.jdevs.timeo.ui.activities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.Task
import com.jdevs.timeo.util.SingleLiveEvent

class AddEditActivityViewModel : ViewModel() {

    val name = MutableLiveData("")
    val nameError get() = _nameError as LiveData<String>
    val activityExists get() = _activityExists as LiveData<Boolean>
    val hideKeyboard = SingleLiveEvent<Any>()
    val showDeleteDialog = SingleLiveEvent<Any>()
    val saveActivity = SingleLiveEvent<String>()

    private val _nameError = MutableLiveData("")
    private val _activityExists = MutableLiveData(false)

    fun setActivity(activity: Task?) {

        name.value = activity?.name.orEmpty()
        _activityExists.value = true
    }

    fun setNameError(error: String) {

        _nameError.value = error
    }

    fun triggerSaveActivity() {

        saveActivity.value = name.value.orEmpty()
    }

    fun showDeleteDialog() {

        showDeleteDialog.call()
    }

    fun hideKeyboard() {

        hideKeyboard.call()
    }
}
