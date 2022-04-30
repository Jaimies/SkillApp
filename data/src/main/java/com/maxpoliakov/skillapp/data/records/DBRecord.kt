@file:UseSerializers(DurationAsLongSerializer::class, LocalDateAsStringSerializer::class)

package com.maxpoliakov.skillapp.data.records

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.maxpoliakov.skillapp.data.serialization.DurationAsLongSerializer
import com.maxpoliakov.skillapp.data.serialization.LocalDateAsStringSerializer
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

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
    val unit: MeasurementUnit = MeasurementUnit.Millis,
    val date: LocalDate = getCurrentDate()
)

fun DBRecord.mapToDomain() = Record(recordName, skillId, time, unit, id, date)
fun Record.mapToDB() = DBRecord(id, count, skillId, name, unit, date)
