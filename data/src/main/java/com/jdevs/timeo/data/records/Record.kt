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
import com.jdevs.timeo.shared.time.toDate
import com.jdevs.timeo.shared.time.toOffsetDate
import org.threeten.bp.OffsetDateTime
import java.util.Date

@Keep
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
    var id: Int = 0,
    var name: String = "",
    var time: Long = 0,
    var activityId: Int = 0,
    var creationDate: OffsetDateTime = OffsetDateTime.now()
)

@Keep
data class FirestoreRecord(
    @DocumentId
    var documentId: String = "",
    var name: String = "",
    var time: Long = 0,
    var activityId: String = "",
    @ServerTimestamp
    var timestamp: Date? = null
)

fun FirestoreRecord.mapToDomain() = Record(
    documentId = documentId,
    name = name,
    time = time,
    activityId = activityId,
    creationDate = timestamp.toOffsetDate()
)

fun DBRecord.mapToDomain() = Record(
    id, name = name, time = time, roomActivityId = activityId, creationDate = creationDate
)

fun Record.mapToDB() = DBRecord(id, name, time, roomActivityId, creationDate)
fun Record.mapToFirestore() =
    FirestoreRecord(documentId, name, time, activityId, creationDate.toDate())
