package com.jdevs.timeo.domain.usecase.tasks

import com.jdevs.timeo.domain.repository.TasksRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private val tasksRepository: TasksRepository) {

    suspend operator fun invoke(name: String, projectId: String) =
        tasksRepository.addTask(name, projectId)
}
