package com.maxpoliakov.skillapp.data.stats

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.maxpoliakov.skillapp.data.activities.DBActivity
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.shared.util.EPOCH
import java.time.Duration

@Entity(
    tableName = "stats",
    primaryKeys = ["day", "activityId"],
    indices = [Index(value = ["activityId"])],
    foreignKeys = [ForeignKey(
        entity = DBActivity::class,
        parentColumns = ["id"],
        childColumns = ["activityId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DBStatistic(
    val day: Long,
    val activityId: Int,
    val time: Duration
)

fun DBStatistic.mapToDomain() = Statistic(EPOCH.plusDays(day), time)
