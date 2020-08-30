package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Project

interface ProjectsRepository {

    val projects: DataSource.Factory<Int, Project>

    val topProjects: LiveData<List<Project>>

    fun getProjectById(id: Int): LiveData<Project>

    suspend fun addProject(name: String, description: String)

    suspend fun saveProject(project: Project)

    suspend fun deleteProject(project: Project)
}
