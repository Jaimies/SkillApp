@file:UseSerializers(LocalDateAsStringSerializer::class)

package com.theskillapp.skillapp.data.stats

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.theskillapp.skillapp.data.serialization.LocalDateAsStringSerializer
import com.theskillapp.skillapp.data.skill.DBSkill
import com.theskillapp.skillapp.domain.model.Statistic
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
    val time: Long,
)

fun DBStatistic.mapToDomain() = Statistic(date, time)
