@file:UseSerializers(
    LocalDateAsStringSerializer::class,
    LocalTimeAsStringSerializer::class,
)

package com.maxpoliakov.skillapp.data.records

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.maxpoliakov.skillapp.data.serialization.DBMeasurementUnit
import com.maxpoliakov.skillapp.data.serialization.DBMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.data.serialization.LocalDateAsStringSerializer
import com.maxpoliakov.skillapp.data.serialization.LocalTimeAsStringSerializer
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import java.time.LocalDate
import java.time.LocalTime

@Serializable
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
    val time: Long = 0,
    val skillId: Int = 0,
    @Transient
    val recordName: String = "",
    val unit: DBMeasurementUnit = DBMeasurementUnit.Millis,
    val date: LocalDate = getCurrentDate(),
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
)

fun DBRecord.mapToDomain() = Record(
    recordName,
    skillId,
    time,
    unit.toDomain(),
    id,
    date,
    if (startTime != null && endTime != null) startTime..endTime else null,
)

fun Record.mapToDB() = DBRecord(
    id,
    count,
    skillId,
    name,
    unit.mapToUI(),
    date,
    timeRange?.start,
    timeRange?.endInclusive
)
