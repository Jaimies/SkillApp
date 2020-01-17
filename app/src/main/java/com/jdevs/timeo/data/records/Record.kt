package com.jdevs.timeo.data.records

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.data.activities.DBActivity
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
