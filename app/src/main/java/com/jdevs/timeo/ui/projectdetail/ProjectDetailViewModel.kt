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
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import com.jdevs.timeo.util.time.getAvgWeekHours
import com.jdevs.timeo.util.time.getDaysSpentSince
import com.jdevs.timeo.util.time.getHours
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProjectDetailViewModel @Inject constructor(
    private val getProjectById: GetProjectByIdUseCase,
    private val addRecord: AddRecordUseCase
) : ViewModel() {

    lateinit var project: LiveData<ProjectItem>

    val name: LiveData<String> get() = _name
    val description: LiveData<String> get() = _description
    val daysSpent: LiveData<String> get() = _daysSpent
    val avgWeekTime: LiveData<String> get() = _avgWeekTime
    val lastWeekTime: LiveData<String> get() = _lastWeekTime
    val totalTime: LiveData<String> get() = _totalTime
    private val _name = MutableLiveData("")
    private val _description = MutableLiveData("")
    private val _daysSpent = MutableLiveData("")
    private val _avgWeekTime = MutableLiveData("")
    private val _lastWeekTime = MutableLiveData("")
    private val _totalTime = MutableLiveData("")

    val showRecordDialog = SingleLiveEvent<Any>()

    fun setProject(project: ProjectItem) {

        _name.value = project.name
        _description.value = project.description
        _daysSpent.value = project.creationDate.getDaysSpentSince().toString()
        _totalTime.value = getHours(project.totalTime) + "h"
        _avgWeekTime.value = getAvgWeekHours(project.totalTime, project.creationDate) + "h"
        _lastWeekTime.value = getHours(project.lastWeekTime) + "h"
    }

    fun setupProjectLiveData(project: ProjectItem) {

        this.project = map(getProjectById(project.id), Project::mapToPresentation)
        setProject(project)
    }

    fun addRecord(project: ProjectItem, time: Long) = viewModelScope.launch {

        val record = Record(name = project.name, time = time, activityId = project.id)
        addRecord(record)
    }

    fun showRecordDialog() = showRecordDialog.call()
}
