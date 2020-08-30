package com.jdevs.timeo.data.stats

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdevs.timeo.domain.model.Statistic

@Entity(tableName = "dayStats")
data class DBDayStats(@PrimaryKey val day: Int = 0, val time: Int = 0)

@Entity(tableName = "weekStats")
data class DBWeekStats(@PrimaryKey val week: Int = 0, val time: Int = 0)

@Entity(tableName = "monthStats")
data class DBMonthStats(@PrimaryKey val month: Int = 0, val time: Int = 0)

fun DBDayStats.mapToDomain() = Statistic(time, day, emptyMap())
fun DBWeekStats.mapToDomain() = Statistic(time, week, emptyMap())
fun DBMonthStats.mapToDomain() = Statistic(time, month, emptyMap())
