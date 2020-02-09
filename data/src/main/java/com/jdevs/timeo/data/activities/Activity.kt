package com.jdevs.timeo.data.activities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.data.firestore.RecordMinimal
import com.jdevs.timeo.data.firestore.Recordable
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.shared.time.toOffsetDate
import org.threeten.bp.OffsetDateTime
import java.util.Date

@Keep
@Entity(tableName = "activities")
data class DBActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val totalTime: Long = 0,
    val lastWeekTime: Int = 0,
    val creationDate: OffsetDateTime = OffsetDateTime.now()
)

@Keep
data class FirestoreActivity(
    @DocumentId
    val documentId: String = "",
    val name: String = "",
    val totalTime: Long = 0,
    override val recentRecords: List<RecordMinimal> = emptyList(),
    @ServerTimestamp
    val timestamp: Date? = null
) : Recordable()

fun DBActivity.mapToDomain() = Activity(id.toString(), name, totalTime, lastWeekTime, creationDate)

fun FirestoreActivity.mapToDomain() =
    Activity(documentId, name, totalTime, getLastWeekTime(), timestamp.toOffsetDate())

fun Activity.mapToDB() = DBActivity(id.toInt(), name, totalTime, lastWeekTime, creationDate)
