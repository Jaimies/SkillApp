package com.jdevs.timeo.data.projects

import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.repository.ProjectsRepository
import com.jdevs.timeo.shared.util.mapList
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultProjectsRepository @Inject constructor(
    private val projectsDao: ProjectsDao
) : ProjectsRepository {

    override val projects by lazy {
        projectsDao.getProjects().map(DBProject::mapToDomain)
    }

    override val topProjects by lazy {
        projectsDao.getTopProjects().mapList { it.mapToDomain() }
    }

    override fun getProjectById(id: Int) =
        projectsDao.getProjectById(id).map { it.mapToDomain() }

    override suspend fun addProject(name: String, description: String) =
        projectsDao.insert(DBProject(name = name, description = description))

    override suspend fun saveProject(project: Project) =
        projectsDao.update(project.mapToDB())

    override suspend fun deleteProject(project: Project) =
        projectsDao.delete(project.mapToDB())
}
