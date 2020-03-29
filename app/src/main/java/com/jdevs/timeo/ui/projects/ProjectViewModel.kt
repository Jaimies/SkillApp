package com.jdevs.timeo.ui.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.util.time.getFriendlyHours

class ProjectViewModel {

    val navigateToDetails = SingleLiveEvent<Any>()
    val showRecordDialog = SingleLiveEvent<Any>()

    val state: LiveData<ProjectState> get() = _state
    private val _state = MutableLiveData<ProjectState>()

    class ProjectState(project: ProjectItem) {
        val name = project.name
        val totalTime = getFriendlyHours(project.totalTime)
    }

    fun setProject(project: ProjectItem) {

        _state.value = ProjectState(project)
    }

    fun navigateToDetails() = navigateToDetails.call()
    fun showRecordDialog() = showRecordDialog.call()
}
