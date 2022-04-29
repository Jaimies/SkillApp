@file:UseSerializers(DurationAsLongSerializer::class, LocalDateAsStringSerializer::class)

package com.maxpoliakov.skillapp.data.stats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.maxpoliakov.skillapp.data.serialization.DurationAsLongSerializer
import com.maxpoliakov.skillapp.data.serialization.LocalDateAsStringSerializer
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.domain.model.Statistic
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

@Serializable
@Entity(
    tableName = "stats",
    primaryKeys = ["date", "skillId"],
    indices = [Index(value = ["skillId"])],
    foreignKeys = [ForeignKey(
        entity = DBSkill::class,
        parentColumns = ["id"],
        childColumns = ["skillId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DBStatistic(
    val date: LocalDate,
    val skillId: Int,
    @ColumnInfo(name = "time")
    val count: Long,
)

fun DBStatistic.mapToDomain() = Statistic(date, count)
