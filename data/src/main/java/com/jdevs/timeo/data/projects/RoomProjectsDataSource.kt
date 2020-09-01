package com.jdevs.timeo.data.projects

import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface ProjectsDataSource {

    fun getTopProjects(): Flow<List<Project>>

    fun getProjectById(id: Int): Flow<Project>

    suspend fun addProject(name: String, description: String)

    suspend fun saveProject(project: Project)

    suspend fun deleteProject(project: Project)
}

interface ProjectsLocalDataSource : ProjectsDataSource {

    val projects: DataSource.Factory<Int, Project>
}

@Singleton
class RoomProjectsDataSource @Inject constructor(private val projectsDao: ProjectsDao) :
    ProjectsLocalDataSource {

    override val projects by lazy {
        projectsDao.getProjects().map(DBProject::mapToDomain)
    }

    override fun getTopProjects() =
        projectsDao.getTopProjects().mapList { it.mapToDomain() }

    override fun getProjectById(id: Int) =
        projectsDao.getProjectById(id).map { it.mapToDomain() }

    override suspend fun addProject(name: String, description: String) =
        projectsDao.insert(DBProject(name = name, description = description))

    override suspend fun saveProject(project: Project) =
        projectsDao.update(project.mapToDB())

    override suspend fun deleteProject(project: Project) =
        projectsDao.delete(project.mapToDB())
}
