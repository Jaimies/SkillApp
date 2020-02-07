package com.jdevs.timeo.ui.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.ui.model.ProjectItem
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import com.jdevs.timeo.util.time.getHours

class ProjectViewModel : ViewModel() {

    val name: LiveData<String> get() = _name
    val totalTime: LiveData<String> get() = _totalTime
    private val _name = MutableLiveData("")
    private val _totalTime = MutableLiveData("")

    val navigateToDetails = SingleLiveEvent<Any>()
    val showRecordDialog = SingleLiveEvent<Any>()

    fun setProject(project: ProjectItem) {

        _name.value = project.name
        _totalTime.value = getHours(project.totalTime) + "h"
    }

    fun navigateToDetails() = navigateToDetails.call()
    fun showRecordDialog() = showRecordDialog.call()
}
