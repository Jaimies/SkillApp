package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.jdevs.timeo.data.firestore.ItemsLiveData
import com.jdevs.timeo.domain.model.Project

interface ProjectsRepository {

    val projects: LiveData<PagedList<Project>>
    val projectsRemote: List<ItemsLiveData>

    val topProjects: LiveData<List<Project>>

    fun getProjectById(id: Int, documentId: String): LiveData<Project>

    suspend fun addProject(project: Project)

    suspend fun saveProject(project: Project)

    suspend fun deleteProject(project: Project)
}
