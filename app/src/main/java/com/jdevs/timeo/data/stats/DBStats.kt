package com.jdevs.timeo.data.stats

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.model.DayStats
import com.jdevs.timeo.model.MonthStats
import com.jdevs.timeo.model.WeekStats

@Entity(tableName = "dayStats")
data class DBDayStats(
    var time: Long = 0,
    @PrimaryKey var day: Long = 0
) : Mapper<DayStats> {

    override fun mapToDomain() = DayStats(time = time, day = day)
}

@Entity(tableName = "weekStats")
data class DBWeekStats(
    var time: Long = 0,
    @PrimaryKey var week: Int = 0
) : Mapper<WeekStats> {

    override fun mapToDomain() = WeekStats(time = time, week = week)
}

@Entity(tableName = "monthStats")
data class DBMonthStats(
    var time: Long = 0,
    @PrimaryKey var month: Int = 0
) : Mapper<MonthStats> {

    override fun mapToDomain() = MonthStats(time = time, month = month)
}
