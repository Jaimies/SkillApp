package com.jdevs.timeo.data.tasks

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.jdevs.timeo.domain.model.Task

@Entity(tableName = "tasks")
data class DBTask(
    @PrimaryKey
    val id: Int,
    val name: String,
    val projectId: Int,
    val timeSpent: Int,
    val isCompleted: Boolean
)

data class FirestoreTask(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val projectId: String = "",
    val timeSpent: Int = 0,
    @get:PropertyName("isCompleted")
    val isCompleted: Boolean = false
)

fun DBTask.mapToDomain() = Task(id.toString(), name, projectId.toString(), timeSpent, isCompleted)

fun Task.mapToDB() = DBTask(id.toInt(), name, projectId.toInt(), timeSpent, isCompleted)

fun FirestoreTask.mapToDomain() = Task(id, name, projectId, timeSpent, isCompleted)

fun Task.mapToFirestore() = FirestoreTask(id, name, projectId, timeSpent, isCompleted)
