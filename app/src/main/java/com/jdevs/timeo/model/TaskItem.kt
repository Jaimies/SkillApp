package com.jdevs.timeo.model

import com.jdevs.timeo.domain.model.Task

data class TaskItem(
    override val id: String,
    val name: String,
    val projectId: String,
    val timeSpent: Int,
    val isCompleted: Boolean
) : ViewItem

fun Task.mapToPresentation() = TaskItem(id, name, projectId, timeSpent, isCompleted)
