package com.jdevs.timeo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.repository.ProjectsRepository
import com.jdevs.timeo.util.ListDataSource
import com.jdevs.timeo.util.createLiveData
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeProjectsRepository @Inject constructor() : ProjectsRepository {

    private val projectsList = mutableListOf<Project>()
    override val projects = ListDataSource.Factory(projectsList)

    override fun getProjectsRemote(fetchNewItems: Boolean) =
        listOf(createLiveData<Operation<Project>>())

    override suspend fun addProject(name: String, description: String) {

        projectsList.add(Project("", name, description, 0, 0, OffsetDateTime.now()))
        notifyObservers()
    }

    override suspend fun deleteProject(project: Project) {

        projectsList.remove(project)
        notifyObservers()
    }

    override val topProjects get() = MutableLiveData(projectsList.toList())

    override fun getProjectById(id: String): LiveData<Project> {

        val project = projectsList.find { it.id == id }!!
        return MutableLiveData(project)
    }

    override suspend fun saveProject(project: Project) {

        projectsList.replaceAll { if (it.id != project.id) it else project }
    }

    fun reset() {

        projectsList.clear()
        notifyObservers()
    }

    private fun notifyObservers() = projects
}
