package com.jdevs.timeo.ui.projectdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.projects.GetProjectByIdUseCase
import com.jdevs.timeo.domain.usecase.records.AddRecordUseCase
import com.jdevs.timeo.ui.model.ProjectItem
import com.jdevs.timeo.ui.model.mapToPresentation
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import com.jdevs.timeo.util.time.getAvgWeekHours
import com.jdevs.timeo.util.time.getHours
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProjectDetailViewModel @Inject constructor(
    private val getProjectById: GetProjectByIdUseCase,
    private val addRecord: AddRecordUseCase
) : ViewModel() {

    lateinit var project: LiveData<ProjectItem>

    val name: LiveData<String> get() = _name
    val avgWeekTime: LiveData<String> get() = _avgWeekTime
    val lastWeekTime: LiveData<String> get() = _lastWeekTime
    val totalTime: LiveData<String> get() = _totalTime
    private val _avgWeekTime = MutableLiveData("")
    private val _lastWeekTime = MutableLiveData("")
    private val _totalTime = MutableLiveData("")
    private val _name = MutableLiveData("")

    val showRecordDialog = SingleLiveEvent<Any>()

    fun setProject(project: ProjectItem) {

        _name.value = project.name
        _totalTime.value = getHours(project.totalTime) + "h"
        _avgWeekTime.value = getAvgWeekHours(project.totalTime, project.creationDate) + "h"
        _lastWeekTime.value = getHours(project.lastWeekTime) + "h"
    }

    fun setupProjectLiveData(project: ProjectItem) {

        this.project =
            map(getProjectById(project.id, project.documentId), Project::mapToPresentation)
        setProject(project)
    }

    fun addRecord(project: ProjectItem, time: Long) = viewModelScope.launch {

        val record = Record(
            name = project.name, time = time,
            activityId = project.documentId, roomActivityId = project.id
        )

        addRecord(record)
    }

    fun showRecordDialog() = showRecordDialog.call()
}
