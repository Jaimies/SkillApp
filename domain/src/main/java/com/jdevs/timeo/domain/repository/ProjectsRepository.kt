package com.jdevs.timeo.domain.repository

import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectsRepository {
    val projects: DataSource.Factory<Id, Project>
    val topProjects: Flow<List<Project>>

    fun getProjectById(id: Id): Flow<Project>

    suspend fun addProject(name: String, description: String)
    suspend fun saveProject(project: Project)
    suspend fun deleteProject(project: Project)
}
