package com.jdevs.timeo.data.projects

import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.repository.ProjectsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultProjectsRepository @Inject constructor(
    private val dataSource: ProjectsLocalDataSource
) : ProjectsRepository {

    override val projects get() = dataSource.projects

    override val topProjects get() = dataSource.getTopProjects()

    override fun getProjectById(id: Int) = dataSource.getProjectById(id)

    override suspend fun addProject(name: String, description: String) =
        dataSource.addProject(name, description)

    override suspend fun saveProject(project: Project) = dataSource.saveProject(project)

    override suspend fun deleteProject(project: Project) = dataSource.deleteProject(project)
}
