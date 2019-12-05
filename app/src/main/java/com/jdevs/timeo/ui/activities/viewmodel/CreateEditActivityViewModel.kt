package com.jdevs.timeo.ui.activities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.TimeoActivity

class CreateEditActivityViewModel : ViewModel() {
    var navigator: Navigator? = null
    val name = MutableLiveData("")
    val icon = MutableLiveData("")
    val nameError get() = _nameError as LiveData<String>
    val iconError get() = _iconError as LiveData<String>
    val activityExists get() = _activityExists as LiveData<Boolean>

    private val _nameError = MutableLiveData("")
    private val _iconError = MutableLiveData("")
    private val _activityExists = MutableLiveData(false)

    fun setActivity(activity: TimeoActivity?) {

        name.value = activity?.name.orEmpty()
        icon.value = activity?.icon.orEmpty()

        _activityExists.value = true
    }

    fun setNameError(error: String) {
        _nameError.value = error
    }

    fun setIconError(error: String) {
        _iconError.value = error
    }

    fun triggerActivitySave() {
        navigator?.saveActivity(name.value.orEmpty(), icon.value.orEmpty())
    }

    interface Navigator {
        fun saveActivity(name: String, icon: String)
        fun showDeleteDialog()
        fun hideKeyboard()
    }
}
