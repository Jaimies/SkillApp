package com.jdevs.timeo.data

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.util.AdapterConstants.RECORD
import org.threeten.bp.OffsetDateTime
import java.util.Date

@Keep
@Entity(
    tableName = "records",
    indices = [Index(value = ["activity_id"])],
    foreignKeys = [ForeignKey(
        entity = Activity::class,
        parentColumns = ["id"],
        childColumns = ["activity_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Record(
    var name: String = "",
    var time: Long = 0,

    @Ignore
    var activityId: String = "",

    @get:Exclude
    @ColumnInfo(name = "activity_id")
    var roomActivityId: Int = 0
) : DataItem() {

    @get:Exclude
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0

    @DocumentId
    override var documentId: String = ""

    @get:Exclude
    @set:Exclude
    override var creationDate: OffsetDateTime = OffsetDateTime.now()

    @Ignore
    @ServerTimestamp
    @get:PropertyName("timestamp")
    @set:PropertyName("timestamp")
    override var firestoreTimestamp: Date? = null

    @Ignore
    @get:Exclude
    override val viewType = RECORD
}
