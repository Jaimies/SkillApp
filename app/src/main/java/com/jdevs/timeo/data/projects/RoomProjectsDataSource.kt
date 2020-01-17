package com.jdevs.timeo.data.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jdevs.timeo.data.db.toLivePagedList
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.util.PagingConstants.PROJECTS_PAGE_SIZE
import javax.inject.Inject
import javax.inject.Singleton

interface ProjectsDataSource {

    val projects: LiveData<*>?

    fun getTopProjects(): LiveData<List<Project>>

    fun getProjectById(id: Int, documentId: String): LiveData<Project>

    suspend fun addProject(project: Project)

    suspend fun saveProject(project: Project)

    suspend fun deleteProject(project: Project)
}

interface ProjectsLocalDataSource : ProjectsDataSource

@Singleton
class RoomProjectsDataSource @Inject constructor(
    private val projectsDao: ProjectsDao,
    private val mapper: DBProjectMapper,
    private val domainMapper: DBDomainProjectMapper
) : ProjectsLocalDataSource {

    override val projects by lazy {

        projectsDao.getProjects().toLivePagedList(PROJECTS_PAGE_SIZE, domainMapper)
    }

    override fun getTopProjects() =
        Transformations.map(projectsDao.getTopProjects()) { it.map(domainMapper::map) }

    override fun getProjectById(id: Int, documentId: String) =
        Transformations.map(projectsDao.getProjectById(id)) { domainMapper.map(it) }

    override suspend fun addProject(project: Project) = projectsDao.insert(mapper.map(project))

    override suspend fun saveProject(project: Project) = projectsDao.update(mapper.map(project))

    override suspend fun deleteProject(project: Project) = projectsDao.delete(mapper.map(project))
}
