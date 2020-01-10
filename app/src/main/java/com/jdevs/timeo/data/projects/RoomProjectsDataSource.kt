package com.jdevs.timeo.data.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jdevs.timeo.data.db.toLivePagedList
import com.jdevs.timeo.model.Project
import com.jdevs.timeo.util.PagingConstants.PROJECTS_PAGE_SIZE
import javax.inject.Inject
import javax.inject.Singleton

interface ProjectsDataSource {

    val projects: LiveData<*>?

    fun getProjectById(id: Int, documentId: String): LiveData<Project>

    suspend fun addProject(project: Project)

    suspend fun saveProject(project: Project)

    suspend fun deleteProject(project: Project)
}

interface ProjectsLocalDataSource : ProjectsDataSource

@Singleton
class RoomProjectsDataSource @Inject constructor(
    private val projectsDao: ProjectsDao
) : ProjectsLocalDataSource {

    override val projects by lazy {

        projectsDao.getProjects().toLivePagedList(PROJECTS_PAGE_SIZE)
    }

    override fun getProjectById(id: Int, documentId: String) =
        Transformations.map(projectsDao.getProjectById(id)) {
            it.mapToDomain()
        }

    override suspend fun addProject(project: Project) = projectsDao.insert(project.toDB())

    override suspend fun saveProject(project: Project) = projectsDao.update(project.toDB())

    override suspend fun deleteProject(project: Project) = projectsDao.delete(project.toDB())
}
