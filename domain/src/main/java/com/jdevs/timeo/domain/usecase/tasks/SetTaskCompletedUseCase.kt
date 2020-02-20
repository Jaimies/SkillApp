package com.jdevs.timeo.domain.usecase.tasks

import com.jdevs.timeo.domain.repository.TasksRepository
import javax.inject.Inject

class SetTaskCompletedUseCase @Inject constructor(private val tasksRepository: TasksRepository) {

    suspend operator fun invoke(taskId: String, isCompleted: Boolean) {

        tasksRepository.setTaskCompleted(taskId, isCompleted)
    }
}
