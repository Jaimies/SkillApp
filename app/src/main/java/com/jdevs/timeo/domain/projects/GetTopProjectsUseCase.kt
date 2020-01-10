package com.jdevs.timeo.domain.projects

import com.jdevs.timeo.data.projects.ProjectsRepository
import javax.inject.Inject

class GetTopProjectsUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) {

    fun getTopProjects() = projectsRepository.getTopProjects()
}
