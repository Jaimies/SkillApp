package com.jdevs.timeo.data.records

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jdevs.timeo.data.activities.DBActivity
import com.jdevs.timeo.domain.model.Record
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
    val timestamp: OffsetDateTime = OffsetDateTime.now()
)

fun DBRecord.mapToDomain() = Record(activityName, activityId, time, id, timestamp)
fun Record.mapToDB() = DBRecord(id, time, activityId, name, timestamp)
