package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Project

interface ProjectsRepository {

    val projects: DataSource.Factory<Int, Project>
    val projectsRemote: List<LiveData<Operation<Project>>>

    val topProjects: LiveData<List<Project>>

    fun getProjectById(id: String): LiveData<Project>

    suspend fun addProject(name: String)

    suspend fun saveProject(project: Project)

    suspend fun deleteProject(project: Project)
}
