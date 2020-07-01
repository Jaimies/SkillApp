package com.jdevs.timeo.ui.addproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.usecase.projects.AddProjectUseCase
import com.jdevs.timeo.domain.usecase.projects.DeleteProjectUseCase
import com.jdevs.timeo.domain.usecase.projects.SaveProjectUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.model.mapToDomain
import com.jdevs.timeo.ui.common.viewmodel.KeyboardHidingViewModel
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import javax.inject.Inject

class AddEditProjectViewModel @Inject constructor(
    private val addProject: AddProjectUseCase,
    private val saveProjectUseCase: SaveProjectUseCase,
    private val deleteProject: DeleteProjectUseCase,
    project: ProjectItem?
) : KeyboardHidingViewModel() {

    val name = MutableLiveData(project?.name)
    val description = MutableLiveData(project?.description)
    val projectExists = project != null

    val nameError get() = _nameError as LiveData<String>
    private val _nameError = MutableLiveData<String>()
    val showDeleteDialog = SingleLiveEvent<Any>()

    fun setNameError(error: String) {
        _nameError.value = error
    }

    fun addProject(name: String, description: String) = launchCoroutine {
        addProject.invoke(name, description)
    }

    fun saveProject(project: ProjectItem) = launchCoroutine {
        saveProjectUseCase.invoke(project.mapToDomain())
    }

    fun deleteProject(project: ProjectItem) = launchCoroutine {
        deleteProject.invoke(project.mapToDomain())
    }

    fun showDeleteDialog() = showDeleteDialog.call()

    class Factory @Inject constructor(
        private val addProject: AddProjectUseCase,
        private val saveProjectUseCase: SaveProjectUseCase,
        private val deleteProject: DeleteProjectUseCase
    ) {
        fun create(project: ProjectItem?) = AddEditProjectViewModel(
            addProject, saveProjectUseCase, deleteProject, project
        )
    }
}
