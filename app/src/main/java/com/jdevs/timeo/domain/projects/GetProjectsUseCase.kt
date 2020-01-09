package com.jdevs.timeo.domain.projects

import com.jdevs.timeo.data.projects.ProjectsRepository
import javax.inject.Inject

class GetProjectsUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) {

    val projects get() = projectsRepository.projects
    fun resetMonitor() = projectsRepository.resetMonitor()
}
