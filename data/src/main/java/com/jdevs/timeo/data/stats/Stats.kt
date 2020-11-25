package com.jdevs.timeo.data.stats

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jdevs.timeo.data.activities.DBActivity
import com.jdevs.timeo.domain.model.Statistic
import com.jdevs.timeo.shared.util.EPOCH
import java.time.Duration

@Entity(
    tableName = "stats",
    indices = [Index(value = ["activityId"])],
    foreignKeys = [ForeignKey(
        entity = DBActivity::class,
        parentColumns = ["id"],
        childColumns = ["activityId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DBStatistic(
    @PrimaryKey
    val day: Long,
    val activityId: Int,
    val time: Duration
)

fun DBStatistic.mapToDomain() = Statistic(EPOCH.plusDays(day), time)
