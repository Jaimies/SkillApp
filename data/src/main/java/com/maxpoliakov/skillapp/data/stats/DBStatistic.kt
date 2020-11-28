package com.maxpoliakov.skillapp.data.stats

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.shared.util.EPOCH
import java.time.Duration

@Entity(
    tableName = "stats",
    primaryKeys = ["day", "skillId"],
    indices = [Index(value = ["skillId"])],
    foreignKeys = [ForeignKey(
        entity = DBSkill::class,
        parentColumns = ["id"],
        childColumns = ["skillId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DBStatistic(
    val day: Long,
    val skillId: Int,
    val time: Duration
)

fun DBStatistic.mapToDomain() = Statistic(EPOCH.plusDays(day), time)
