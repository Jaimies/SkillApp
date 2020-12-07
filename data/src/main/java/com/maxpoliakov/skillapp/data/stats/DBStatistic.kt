package com.maxpoliakov.skillapp.data.stats

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.domain.model.Statistic
import java.time.Duration
import java.time.LocalDate

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
    val time: Duration
)

fun DBStatistic.mapToDomain() = Statistic(date, time)
