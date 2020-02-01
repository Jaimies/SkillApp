package com.jdevs.timeo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.repository.ProjectsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeProjectsRepository @Inject constructor() : ProjectsRepository {

    private val projectsList = mutableListOf<Project>()
    override val projects = MutableLiveData(projectsList.asPagedList())

    override suspend fun addProject(project: Project) {

        projectsList.add(project)
        notifyObservers()
    }

    override suspend fun deleteProject(project: Project) {

        projectsList.remove(project)
        notifyObservers()
    }

    override val topProjects get() = MutableLiveData(projectsList.toList())

    override fun getProjectById(id: Int, documentId: String): LiveData<Project> {

        val project = projectsList.find { it.id == id } ?: Project(name = "Project")
        return MutableLiveData(project)
    }

    override suspend fun saveProject(project: Project) {

        projectsList.replaceAll { if (it.documentId != project.documentId) it else project }
    }

    fun reset() {

        projectsList.clear()
        notifyObservers()
    }

    override fun resetMonitor() {}

    private fun notifyObservers() = projects.postValue(projectsList.asPagedList())
}
