package com.jdevs.timeo.data.projects

import com.jdevs.timeo.data.Repository
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.domain.repository.ProjectsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultProjectsRepository @Inject constructor(
    remoteDataSource: ProjectsRemoteDataSource,
    localDataSource: ProjectsLocalDataSource,
    authRepository: AuthRepository
) : Repository<ProjectsDataSource, ProjectsRemoteDataSource>(
    remoteDataSource, localDataSource, authRepository
), ProjectsRepository {

    override val projects get() = currentDataSource.projects
    override val topProjects get() = currentDataSource.getTopProjects()

    override fun getProjectById(id: Int, documentId: String) =
        currentDataSource.getProjectById(id, documentId)

    override suspend fun addProject(project: Project) = currentDataSource.addProject(project)

    override suspend fun saveProject(project: Project) = currentDataSource.saveProject(project)

    override suspend fun deleteProject(project: Project) = currentDataSource.deleteProject(project)

    override fun resetMonitor() = performOnRemote { it.resetMonitor() }
}
