package com.jdevs.timeo.ui.activities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.TimeoActivity

class CreateEditActivityViewModel : ViewModel() {

    var navigator: Navigator? = null
    val name = MutableLiveData("")
    val nameError get() = _nameError as LiveData<String>
    val activityExists get() = _activityExists as LiveData<Boolean>

    private val _nameError = MutableLiveData("")
    private val _activityExists = MutableLiveData(false)

    fun setActivity(activity: TimeoActivity?) {

        name.value = activity?.name.orEmpty()
        _activityExists.value = true
    }

    fun setNameError(error: String) {

        _nameError.value = error
    }

    fun triggerSaveActivity() {

        navigator?.saveActivity(name.value.orEmpty())
    }

    interface Navigator {
        fun saveActivity(name: String)
        fun showDeleteDialog()
        fun hideKeyboard()
    }
}
