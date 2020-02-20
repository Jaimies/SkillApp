package com.jdevs.timeo.ui.projectdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.model.Task
import com.jdevs.timeo.domain.usecase.projects.GetProjectByIdUseCase
import com.jdevs.timeo.domain.usecase.tasks.GetTopTasksUseCase
import com.jdevs.timeo.domain.usecase.tasks.SetTaskCompletedUseCase
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.shared.util.map
import com.jdevs.timeo.util.launchCoroutine
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import com.jdevs.timeo.util.time.getAvgWeekHours
import com.jdevs.timeo.util.time.getDaysSpentSince
import com.jdevs.timeo.util.time.getHours
import javax.inject.Inject

class ProjectDetailViewModel @Inject constructor(
    private val getProjectById: GetProjectByIdUseCase,
    private val setTaskCompleted: SetTaskCompletedUseCase,
    getTopTasks: GetTopTasksUseCase
) : ViewModel() {

    lateinit var project: LiveData<ProjectItem>

    val topTasks by lazy { map(getTopTasks(), Task::mapToPresentation) }
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

    val goToTasks = SingleLiveEvent<Any>()

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

    fun setTaskCompleted(position: Int, isCompleted: Boolean) = launchCoroutine {

        setTaskCompleted.invoke(topTasks.value!![position].id, isCompleted)
    }

    fun goToTasks() = goToTasks.call()
}
