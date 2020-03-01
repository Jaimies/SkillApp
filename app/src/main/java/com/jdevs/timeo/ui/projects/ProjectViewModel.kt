package com.jdevs.timeo.ui.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import com.jdevs.timeo.util.time.getFriendlyHours

class ProjectViewModel {

    val name: LiveData<String> get() = _name
    val totalTime: LiveData<String> get() = _totalTime
    private val _name = MutableLiveData("")
    private val _totalTime = MutableLiveData("")

    val navigateToDetails = SingleLiveEvent<Any>()
    val showRecordDialog = SingleLiveEvent<Any>()

    fun setProject(project: ProjectItem) {

        _name.value = project.name
        _totalTime.value = getFriendlyHours(project.totalTime) + "h"
    }

    fun navigateToDetails() = navigateToDetails.call()
    fun showRecordDialog() = showRecordDialog.call()
}
