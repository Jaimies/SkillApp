package com.jdevs.timeo.domain.usecase.tasks

import com.jdevs.timeo.domain.repository.TasksRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(private val tasksRepository: TasksRepository) {

    val tasks get() = tasksRepository.tasks
    fun getRemoteTasks(fetchNewItems: Boolean) = tasksRepository.getRemoteTasks(fetchNewItems)
}
