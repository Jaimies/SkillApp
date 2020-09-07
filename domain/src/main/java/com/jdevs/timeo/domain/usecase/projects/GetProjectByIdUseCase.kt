package com.jdevs.timeo.domain.usecase.projects

import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.repository.ProjectsRepository
import javax.inject.Inject

class GetProjectByIdUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) {
    fun run(id: Id) = projectsRepository.getProjectById(id)
}
