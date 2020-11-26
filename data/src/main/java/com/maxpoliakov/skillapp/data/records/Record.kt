package com.maxpoliakov.skillapp.data.records

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.maxpoliakov.skillapp.data.activities.DBActivity
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.shared.util.getCurrentDateTime
import java.time.Duration
import java.time.OffsetDateTime

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
    val time: Duration = Duration.ZERO,
    val activityId: Int = 0,
    val activityName: String = "",
    val timestamp: OffsetDateTime = getCurrentDateTime()
)

fun DBRecord.mapToDomain() = Record(activityName, activityId, time, id, timestamp)
fun Record.mapToDB() = DBRecord(id, time, activityId, name, timestamp)
