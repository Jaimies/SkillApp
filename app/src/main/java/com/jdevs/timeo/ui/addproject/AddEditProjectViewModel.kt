package com.jdevs.timeo.ui.addproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.usecase.projects.AddProjectUseCase
import com.jdevs.timeo.domain.usecase.projects.DeleteProjectUseCase
import com.jdevs.timeo.domain.usecase.projects.SaveProjectUseCase
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.model.mapToDomain
import com.jdevs.timeo.ui.common.viewmodel.KeyboardHidingViewModel
import com.jdevs.timeo.util.launchCoroutine
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import javax.inject.Inject

class AddEditProjectViewModel @Inject constructor(
    private val addProject: AddProjectUseCase,
    private val saveProjectUseCase: SaveProjectUseCase,
    private val deleteProject: DeleteProjectUseCase
) : KeyboardHidingViewModel() {

    val name = MutableLiveData("")
    val description = MutableLiveData("")
    val nameError get() = _nameError as LiveData<String>
    val projectExists get() = _projectExists as LiveData<Boolean>
    private val _nameError = MutableLiveData("")
    private val _projectExists = MutableLiveData(false)

    val showDeleteDialog = SingleLiveEvent<Any>()

    fun setProject(project: ProjectItem?) {

        name.value = project?.name.orEmpty()
        description.value = project?.description.orEmpty()
        _projectExists.value = true
    }

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
}
