package com.jdevs.timeo.ui.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.util.SingleLiveEvent
import com.jdevs.timeo.util.time.toHours

class ProjectViewModel : ViewModel() {

    val name: LiveData<String> get() = _name
    val totalTime: LiveData<String> get() = _totalTime
    private val _name = MutableLiveData("")
    private val _totalTime = MutableLiveData("")

    val navigateToDetails = SingleLiveEvent<Any>()
    val showRecordDialog = SingleLiveEvent<Any>()

    fun setProject(project: Project) {

        _name.value = project.name
        _totalTime.value = project.totalTime.toHours() + "h"
    }

    fun navigateToDetails() = navigateToDetails.call()
    fun showRecordDialog() = showRecordDialog.call()
}
