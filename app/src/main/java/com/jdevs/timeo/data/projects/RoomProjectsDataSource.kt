package com.jdevs.timeo.data.projects

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.db.toLivePagedList
import com.jdevs.timeo.model.Project
import com.jdevs.timeo.util.PagingConstants.PROJECTS_PAGE_SIZE
import javax.inject.Inject
import javax.inject.Singleton

interface ProjectsDataSource {

    val projects: LiveData<*>?

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

    override suspend fun addProject(project: Project) = projectsDao.insert(project.toDB())

    override suspend fun saveProject(project: Project) = projectsDao.update(project.toDB())

    override suspend fun deleteProject(project: Project) = projectsDao.delete(project.toDB())
}
