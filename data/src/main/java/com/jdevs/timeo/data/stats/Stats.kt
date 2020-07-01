package com.jdevs.timeo.data.stats

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdevs.timeo.domain.model.ActivityTimes
import com.jdevs.timeo.domain.model.Statistic

@Keep
data class FirestoreStat(
    val day: Int = 0,
    val totalTime: Int = 0,
    val activityTimes: ActivityTimes = emptyMap()
)

@Entity(tableName = "dayStats")
data class DBDayStats(@PrimaryKey val day: Int = 0, val time: Int = 0)

@Entity(tableName = "weekStats")
data class DBWeekStats(@PrimaryKey val week: Int = 0, val time: Int = 0)

@Entity(tableName = "monthStats")
data class DBMonthStats(@PrimaryKey val month: Int = 0, val time: Int = 0)

fun DBDayStats.mapToDomain() = Statistic(time, day, emptyMap())
fun DBWeekStats.mapToDomain() = Statistic(time, week, emptyMap())
fun DBMonthStats.mapToDomain() = Statistic(time, month, emptyMap())
fun FirestoreStat.mapToDomain() = Statistic(totalTime, day, activityTimes)
