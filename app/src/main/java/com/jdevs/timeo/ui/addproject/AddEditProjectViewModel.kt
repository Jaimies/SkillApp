package com.jdevs.timeo.ui.addproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.usecase.projects.AddProjectUseCase
import com.jdevs.timeo.domain.usecase.projects.DeleteProjectUseCase
import com.jdevs.timeo.domain.usecase.projects.SaveProjectUseCase
import com.jdevs.timeo.ui.common.viewmodel.KeyboardHidingViewModel
import com.jdevs.timeo.util.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditProjectViewModel @Inject constructor(
    private val addProject: AddProjectUseCase,
    private val saveProjectUseCase: SaveProjectUseCase,
    private val deleteProject: DeleteProjectUseCase
) : KeyboardHidingViewModel() {

    val name = MutableLiveData("")
    val nameError get() = _nameError as LiveData<String>
    val projectExists get() = _projectExists as LiveData<Boolean>
    private val _nameError = MutableLiveData("")
    private val _projectExists = MutableLiveData(false)

    val showDeleteDialog = SingleLiveEvent<Any>()
    val saveProject = SingleLiveEvent<String>()

    fun setProject(project: Project?) {

        name.value = project?.name.orEmpty()
        _projectExists.value = true
    }

    fun setNameError(error: String) {

        _nameError.value = error
    }

    fun triggerSaveProject() {

        saveProject.value = name.value.orEmpty()
    }

    fun addProject(project: Project) = viewModelScope.launch {

        addProject.invoke(project)
    }

    fun saveProject(project: Project) = viewModelScope.launch {

        saveProjectUseCase.invoke(project)
    }

    fun deleteProject(project: Project) = viewModelScope.launch {

        deleteProject.invoke(project)
    }

    fun showDeleteDialog() = showDeleteDialog.call()
}
