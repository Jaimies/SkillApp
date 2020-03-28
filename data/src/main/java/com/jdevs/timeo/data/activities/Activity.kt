package com.jdevs.timeo.data.activities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.data.firestore.RecordMinimal
import com.jdevs.timeo.data.firestore.Recordable
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.shared.util.toDate
import com.jdevs.timeo.shared.util.toOffsetDate
import org.threeten.bp.OffsetDateTime
import java.util.Date

@Entity(tableName = "activities")
data class DBActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val totalTime: Int = 0,
    val lastWeekTime: Int = 0,
    val creationDate: OffsetDateTime = OffsetDateTime.now()
)

@Keep
data class FirestoreActivity(
    @DocumentId
    val documentId: String = "",
    val name: String = "",
    val totalTime: Int = 0,
    override val recentRecords: List<RecordMinimal> = emptyList(),
    val parentActivityName: String = "",
    val parentActivityId: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
) : Recordable()

fun DBActivity.mapToDomain() = Activity(id.toString(), name, totalTime, lastWeekTime, creationDate)

fun FirestoreActivity.mapToDomain() = Activity(
    documentId, name, totalTime, lastWeekTime, timestamp.toOffsetDate(),
    parentActivityName, parentActivityId
)

fun Activity.mapToDB() = DBActivity(id.toInt(), name, totalTime, lastWeekTime, creationDate)
fun Activity.mapToFirestore() = FirestoreActivity(
    id, name, totalTime, emptyList(), parentActivityName, parentActivityId, creationDate.toDate()
)
