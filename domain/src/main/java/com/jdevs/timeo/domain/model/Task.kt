package com.jdevs.timeo.domain.model

data class Task(
    val id: String,
    val name: String,
    val projectId: String,
    val timeSpent: Int,
    val isCompleted: Boolean
)
