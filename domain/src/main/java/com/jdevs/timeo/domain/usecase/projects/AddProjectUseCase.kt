package com.jdevs.timeo.domain.usecase.projects

import com.jdevs.timeo.domain.repository.ProjectsRepository
import javax.inject.Inject

class AddProjectUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) {
    suspend fun run(name: String, description: String) =
        projectsRepository.addProject(name, description)
}
