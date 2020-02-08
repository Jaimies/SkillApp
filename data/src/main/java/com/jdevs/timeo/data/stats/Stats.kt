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
data class DBDayStats(val time: Long = 0, @PrimaryKey val day: Long = 0)

@Entity(tableName = "weekStats")
data class DBWeekStats(val time: Long = 0, @PrimaryKey val week: Int = 0)

@Entity(tableName = "monthStats")
data class DBMonthStats(val time: Long = 0, @PrimaryKey val month: Int = 0)

fun DBDayStats.mapToDomain() = DayStats(time = time, day = day)
fun DBWeekStats.mapToDomain() = WeekStats(time = time, week = week)
fun DBMonthStats.mapToDomain() = MonthStats(time = time, month = month)
fun FirestoreDayStats.mapToDomain() = DayStats(documentId, time, day)
fun FirestoreWeekStats.mapToDomain() = WeekStats(documentId, time, day)
fun FirestoreMonthStats.mapToDomain() = MonthStats(documentId, time, day)
