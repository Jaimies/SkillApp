package com.jdevs.timeo.domain.projects

import com.jdevs.timeo.data.projects.ProjectsRepository
import javax.inject.Inject

class GetProjectByIdUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) {

    fun getProjectById(id: Int, documentId: String) =
        projectsRepository.getProjectById(id, documentId)
}
