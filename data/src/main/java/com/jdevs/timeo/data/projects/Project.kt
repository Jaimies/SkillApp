package com.jdevs.timeo.data.projects

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.data.firestore.RecordMinimal
import com.jdevs.timeo.data.firestore.Recordable
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.shared.util.toDate
import com.jdevs.timeo.shared.util.toOffsetDate
import org.threeten.bp.OffsetDateTime
import java.util.Date

@Entity(tableName = "projects")
data class DBProject(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val totalTime: Long = 0,
    val lastWeekTime: Int = 0,
    val creationDate: OffsetDateTime = OffsetDateTime.now()
)

@Keep
data class FirestoreProject(
    @DocumentId
    val documentId: String = "",
    val name: String = "",
    val description: String = "",
    val totalTime: Long = 0,
    override val recentRecords: List<RecordMinimal> = emptyList(),
    @ServerTimestamp
    val timestamp: Date? = null
) : Recordable()

fun Project.mapToFirestore() =
    FirestoreProject(id, name, description, totalTime, timestamp = creationDate.toDate())

fun Project.mapToDB() =
    DBProject(id.toInt(), name, description, totalTime, lastWeekTime, creationDate)

fun FirestoreProject.mapToDomain() =
    Project(documentId, name, description, totalTime, getLastWeekTime(), timestamp.toOffsetDate())

fun DBProject.mapToDomain() =
    Project(id.toString(), name, description, totalTime, lastWeekTime, creationDate)
