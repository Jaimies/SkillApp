package com.jdevs.timeo.domain.projects

import com.jdevs.timeo.data.projects.ProjectsRepository
import com.jdevs.timeo.model.Project
import javax.inject.Inject

class SaveProjectUseCase @Inject constructor(private val projectsRepository: ProjectsRepository) {

    suspend fun saveProject(project: Project) = projectsRepository.saveProject(project)
}
