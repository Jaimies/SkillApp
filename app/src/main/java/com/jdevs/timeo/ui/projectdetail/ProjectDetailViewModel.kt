package com.jdevs.timeo.ui.projectdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.domain.projects.GetProjectByIdUseCase
import com.jdevs.timeo.domain.records.AddRecordUseCase
import com.jdevs.timeo.model.Project
import com.jdevs.timeo.model.Record
import com.jdevs.timeo.util.SingleLiveEvent
import com.jdevs.timeo.util.time.getAvgDailyHours
import com.jdevs.timeo.util.time.toHours
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProjectDetailViewModel @Inject constructor(
    private val getActivityByIdUseCase: GetProjectByIdUseCase,
    private val addRecordUseCase: AddRecordUseCase
) : ViewModel() {

    val name: LiveData<String> get() = _name
    val avgDailyTime: LiveData<String> get() = _avgDailyTime
    val lastWeekTime: LiveData<String> get() = _lastWeekTime
    val totalTime: LiveData<String> get() = _totalTime
    lateinit var project: LiveData<Project>

    val showRecordDialog = SingleLiveEvent<Any>()
    private val _name = MutableLiveData("")
    private val _avgDailyTime = MutableLiveData("")
    private val _lastWeekTime = MutableLiveData("")
    private val _totalTime = MutableLiveData("")

    fun setProject(project: Project) {

        _name.value = project.name
        _totalTime.value = project.totalTime.toHours() + "h"
        _avgDailyTime.value = project.totalTime.getAvgDailyHours(project.creationDate) + "h"
        _lastWeekTime.value = project.lastWeekTime.toHours() + "h"
    }

    fun setupProjectLiveData(project: Project) {

        this.project = getActivityByIdUseCase.getProjectById(project.id, project.documentId)
    }

    fun addRecord(project: Project, time: Long) = viewModelScope.launch {

        val record = Record(
            name = project.name,
            time = time,
            activityId = project.documentId,
            roomActivityId = project.id
        )

        addRecordUseCase.addRecord(record)
    }

    fun showRecordDialog() = showRecordDialog.call()
}
