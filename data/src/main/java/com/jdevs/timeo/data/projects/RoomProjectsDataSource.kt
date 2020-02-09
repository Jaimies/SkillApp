package com.jdevs.timeo.data.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Project
import javax.inject.Inject
import javax.inject.Singleton

interface ProjectsDataSource {

    fun getTopProjects(): LiveData<List<Project>>

    fun getProjectById(id: Int, documentId: String): LiveData<Project>

    suspend fun addProject(name: String)

    suspend fun saveProject(project: Project)

    suspend fun deleteProject(project: Project)
}

interface ProjectsLocalDataSource : ProjectsDataSource {

    val projects: DataSource.Factory<Int, Project>
}

@Singleton
class RoomProjectsDataSource @Inject constructor(private val projectsDao: ProjectsDao) :
    ProjectsLocalDataSource {

    override val projects by lazy { projectsDao.getProjects().map(DBProject::mapToDomain) }

    override fun getTopProjects() =
        map(projectsDao.getTopProjects()) { it.map(DBProject::mapToDomain) }

    override fun getProjectById(id: Int, documentId: String) =
        map(projectsDao.getProjectById(id), DBProject::mapToDomain)

    override suspend fun addProject(name: String) = projectsDao.insert(DBProject(name = name))

    override suspend fun saveProject(project: Project) = projectsDao.update(project.mapToDB())

    override suspend fun deleteProject(project: Project) = projectsDao.delete(project.mapToDB())
}