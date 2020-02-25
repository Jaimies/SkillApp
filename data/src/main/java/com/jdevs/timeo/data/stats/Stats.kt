package com.jdevs.timeo.data.stats

import androidx.annotation.Keep
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

@Keep
data class FirestoreDayStats(val time: Int = 0, val day: Int = 0) : FirestoreStats()

@Keep
data class FirestoreWeekStats(val time: Int = 0, val day: Int = 0) : FirestoreStats()

@Keep
data class FirestoreMonthStats(val time: Int = 0, val day: Int = 0) : FirestoreStats()

@Entity(tableName = "dayStats")
data class DBDayStats(@PrimaryKey val day: Int = 0, val time: Int = 0)

@Entity(tableName = "weekStats")
data class DBWeekStats(@PrimaryKey val week: Int = 0, val time: Int = 0)

@Entity(tableName = "monthStats")
data class DBMonthStats(@PrimaryKey val month: Int = 0, val time: Int = 0)

fun DBDayStats.mapToDomain() = DayStats(day.toString(), time, day)
fun DBWeekStats.mapToDomain() = WeekStats(week.toString(), time, week)
fun DBMonthStats.mapToDomain() = MonthStats(month.toString(), time, month)
fun FirestoreDayStats.mapToDomain() = DayStats(documentId, time, day)
fun FirestoreWeekStats.mapToDomain() = WeekStats(documentId, time, day)
fun FirestoreMonthStats.mapToDomain() = MonthStats(documentId, time, day)
