package com.jdevs.timeo.data.stats

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdevs.timeo.domain.model.ActivityTimes
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.WeekStats

@Keep
data class FirestoreStats(
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

fun DBDayStats.mapToDomain() = DayStats(time, day, emptyMap())
fun DBWeekStats.mapToDomain() = WeekStats(time, week, emptyMap())
fun DBMonthStats.mapToDomain() = MonthStats(time, month, emptyMap())
fun FirestoreStats.toDayStats() = DayStats(totalTime, day, activityTimes)
fun FirestoreStats.toWeekStats() = WeekStats(totalTime, day, activityTimes)
fun FirestoreStats.toMonthStats() = MonthStats(totalTime, day, activityTimes)
