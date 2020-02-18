package com.jdevs.timeo.domain.usecase.tasks

import com.jdevs.timeo.domain.repository.TasksRepository
import javax.inject.Inject

class GetTopTasksUseCase @Inject constructor(private val tasksRepository: TasksRepository) {

    operator fun invoke() = tasksRepository.getTopTasks()
}
