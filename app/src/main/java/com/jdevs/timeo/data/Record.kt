package com.jdevs.timeo.data

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.jdevs.timeo.common.adapter.ViewType
import com.jdevs.timeo.util.AdapterConstants.RECORD
import java.util.Calendar
import java.util.Date

@Keep
@Entity(
    tableName = "records",
    foreignKeys = [ForeignKey(
        entity = Activity::class,
        parentColumns = ["id"],
        childColumns = ["activity_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Record(
    @Exclude
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0,

    @DocumentId
    override var documentId: String,

    @Ignore
    var name: String = "",
    var time: Long = 0,

    @Exclude
    @ColumnInfo(name = "activity_id")
    var roomActivityId: Int = 0,

    @Ignore
    var activityId: String = "",

    @Ignore
    var timestamp: Date = Calendar.getInstance().time
) : ViewType {

    override fun getViewType() = RECORD
}

data class RecordAndActivity(
    @Embedded
    var record: Record,
    @Relation(
        parentColumn = "activity_id",
        entityColumn = "id"
    )
    var activity: Activity
)
