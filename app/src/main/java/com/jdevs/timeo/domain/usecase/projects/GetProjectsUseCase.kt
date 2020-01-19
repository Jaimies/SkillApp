package com.jdevs.timeo.domain.usecase.projects

import com.jdevs.timeo.domain.repository.ProjectsRepository
import javax.inject.Inject

class GetProjectsUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) {

    operator fun invoke() = projectsRepository.projects
    fun resetMonitor() = projectsRepository.resetMonitor()
}