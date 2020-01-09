package com.jdevs.timeo.data.projects

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.Repository
import com.jdevs.timeo.data.auth.AuthRepository
import com.jdevs.timeo.model.Project
import javax.inject.Inject
import javax.inject.Singleton

interface ProjectsRepository {

    val projects: LiveData<*>?

    suspend fun addProject(project: Project)

    suspend fun saveProject(project: Project)

    suspend fun deleteProject(project: Project)

    fun resetMonitor()
}

@Singleton
class DefaultProjectsRepository @Inject constructor(
    remoteDataSource: ProjectsRemoteDataSource,
    localDataSource: ProjectsLocalDataSource,
    authRepository: AuthRepository
) : Repository<ProjectsDataSource, ProjectsRemoteDataSource>(
    remoteDataSource, localDataSource, authRepository
), ProjectsRepository {

    override val projects get() = currentDataSource.projects

    override suspend fun addProject(project: Project) = currentDataSource.addProject(project)

    override suspend fun saveProject(project: Project) = currentDataSource.deleteProject(project)

    override suspend fun deleteProject(project: Project) = currentDataSource.deleteProject(project)

    override fun resetMonitor() = performOnRemote { it.resetMonitor() }
}
