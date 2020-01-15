package com.jdevs.timeo.domain.usecase.projects

import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.repository.ProjectsRepository
import javax.inject.Inject

class AddProjectUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) {

    suspend fun addProject(project: Project) = projectsRepository.addProject(project)
}
