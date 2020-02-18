package com.jdevs.timeo.model

import com.jdevs.timeo.domain.model.Task
import com.jdevs.timeo.model.ViewType.TASK

data class TaskItem(
    override val id: String,
    val name: String,
    val projectId: String,
    val timeSpent: Int,
    val isCompleted: Boolean
) : ViewItem {

    override val viewType = TASK
}

fun Task.mapToPresentation() = TaskItem(id, name, projectId, timeSpent, isCompleted)
