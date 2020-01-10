package com.jdevs.timeo.domain.projects

import com.jdevs.timeo.data.projects.ProjectsRepository
import com.jdevs.timeo.model.Project
import javax.inject.Inject

class AddProjectUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) {

    suspend fun addProject(project: Project) = projectsRepository.addProject(project)
}
