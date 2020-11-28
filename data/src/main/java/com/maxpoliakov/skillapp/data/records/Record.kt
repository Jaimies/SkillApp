package com.maxpoliakov.skillapp.data.records

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.shared.util.getCurrentDateTime
import java.time.Duration
import java.time.LocalDateTime

@Entity(
    tableName = "records",
    indices = [Index(value = ["skillId"])],
    foreignKeys = [ForeignKey(
        entity = DBSkill::class,
        parentColumns = ["id"],
        childColumns = ["skillId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DBRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: Duration = Duration.ZERO,
    val skillId: Int = 0,
    val recordName: String = "",
    val timestamp: LocalDateTime = getCurrentDateTime()
)

fun DBRecord.mapToDomain() = Record(recordName, skillId, time, id, timestamp)
fun Record.mapToDB() = DBRecord(id, time, skillId, name, timestamp)
