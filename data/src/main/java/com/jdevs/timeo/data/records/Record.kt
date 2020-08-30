package com.jdevs.timeo.data.records

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jdevs.timeo.data.activities.DBActivity
import com.jdevs.timeo.domain.model.Record
import org.threeten.bp.OffsetDateTime

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

fun DBRecord.mapToDomain() = Record(id, name, time, activityId, creationDate)
fun Record.mapToDB() = DBRecord(id, name, time, activityId, creationDate)
