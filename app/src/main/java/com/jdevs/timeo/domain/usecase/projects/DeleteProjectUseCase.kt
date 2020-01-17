package com.jdevs.timeo.domain.usecase.projects

import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.domain.repository.ProjectsRepository
import javax.inject.Inject

class DeleteProjectUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) {

    suspend operator fun invoke(project: Project) = projectsRepository.deleteProject(project)
}
