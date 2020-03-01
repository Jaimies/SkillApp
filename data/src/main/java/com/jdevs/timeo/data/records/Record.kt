package com.jdevs.timeo.data.records

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.data.activities.DBActivity
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.shared.util.toDate
import com.jdevs.timeo.shared.util.toOffsetDate
import org.threeten.bp.OffsetDateTime
import java.util.Date

@Entity(
    tableName = "records",
    indices = [Index(value = ["activityId"])],
    foreignKeys = [ForeignKey(
        entity = DBActivity::class,
        parentColumns = ["id"],
        childColumns = ["activityId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DBRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val time: Int = 0,
    val activityId: Int = 0,
    val creationDate: OffsetDateTime = OffsetDateTime.now()
)

@Keep
data class FirestoreRecord(
    @DocumentId
    val documentId: String = "",
    val name: String = "",
    val time: Int = 0,
    val activityId: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)

fun FirestoreRecord.mapToDomain() =
    Record(documentId, name, time, activityId, timestamp.toOffsetDate())

fun DBRecord.mapToDomain() = Record(id.toString(), name, time, activityId.toString(), creationDate)

fun Record.mapToDB() = DBRecord(id.toIntOrNull() ?: 0, name, time, activityId.toInt(), creationDate)

fun Record.mapToFirestore() = FirestoreRecord(id, name, time, activityId, creationDate.toDate())
