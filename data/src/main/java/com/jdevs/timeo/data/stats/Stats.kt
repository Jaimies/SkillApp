package com.jdevs.timeo.data.stats

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.WeekStats

@Suppress("UnnecessaryAbstractClass")
abstract class FirestoreStats {

    @DocumentId
    val documentId = ""
}

data class FirestoreDayStats(val time: Long = 0, val day: Long = 0) : FirestoreStats()
data class FirestoreWeekStats(val time: Long = 0, val day: Int = 0) : FirestoreStats()
data class FirestoreMonthStats(val time: Long = 0, val day: Int = 0) : FirestoreStats()

@Entity(tableName = "dayStats")
data class DBDayStats(@PrimaryKey val day: Long = 0, val time: Long = 0)

@Entity(tableName = "weekStats")
data class DBWeekStats(@PrimaryKey val week: Int = 0, val time: Long = 0)

@Entity(tableName = "monthStats")
data class DBMonthStats(@PrimaryKey val month: Int = 0, val time: Long = 0)

fun DBDayStats.mapToDomain() = DayStats(day.toString(), time, day)
fun DBWeekStats.mapToDomain() = WeekStats(week.toString(), time, week)
fun DBMonthStats.mapToDomain() = MonthStats(month.toString(), time, month)
fun FirestoreDayStats.mapToDomain() = DayStats(documentId, time, day)
fun FirestoreWeekStats.mapToDomain() = WeekStats(documentId, time, day)
fun FirestoreMonthStats.mapToDomain() = MonthStats(documentId, time, day)
