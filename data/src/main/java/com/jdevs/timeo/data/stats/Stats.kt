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
    var documentId = ""
}

data class FirestoreDayStats(
    var time: Long = 0,
    var day: Long = 0
) : FirestoreStats()

data class FirestoreWeekStats(
    var time: Long = 0,
    val day: Int = 0
) : FirestoreStats()

data class FirestoreMonthStats(
    var time: Long = 0,
    val day: Int = 0
) : FirestoreStats()

@Entity(tableName = "dayStats")
data class DBDayStats(
    var time: Long = 0,
    @PrimaryKey var day: Long = 0
)

@Entity(tableName = "weekStats")
data class DBWeekStats(
    var time: Long = 0,
    @PrimaryKey var week: Int = 0
)

@Entity(tableName = "monthStats")
data class DBMonthStats(
    var time: Long = 0,
    @PrimaryKey var month: Int = 0
)

fun DBDayStats.mapToDomain() = DayStats(time = time, day = day)
fun DBWeekStats.mapToDomain() = WeekStats(time = time, week = week)
fun DBMonthStats.mapToDomain() = MonthStats(time = time, month = month)
fun FirestoreDayStats.mapToDomain() = DayStats(documentId, time, day)
fun FirestoreWeekStats.mapToDomain() = WeekStats(documentId, time, day)
fun FirestoreMonthStats.mapToDomain() = MonthStats(documentId, time, day)
