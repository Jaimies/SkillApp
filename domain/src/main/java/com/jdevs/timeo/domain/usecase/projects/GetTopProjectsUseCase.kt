package com.jdevs.timeo.domain.usecase.projects

import com.jdevs.timeo.domain.repository.ProjectsRepository
import javax.inject.Inject

class GetTopProjectsUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) {
    fun run() = projectsRepository.topProjects
}
