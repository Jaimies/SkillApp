package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import com.jdevs.timeo.domain.model.Project

interface ProjectsRepository {

    val projects: LiveData<*>?

    val topProjects: LiveData<List<Project>>

    fun getProjectById(id: Int, documentId: String): LiveData<Project>

    suspend fun addProject(project: Project)

    suspend fun saveProject(project: Project)

    suspend fun deleteProject(project: Project)

    fun resetMonitor()
}
