package com.jdevs.timeo.data.firestore.model

import com.google.firebase.firestore.DocumentId
import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.WeekStats

@Suppress("UnnecessaryAbstractClass")
abstract class Stats {

    @DocumentId
    var documentId = ""
}

data class FirestoreDayStats(
    var time: Long = 0,
    var day: Long = 0
) : Stats(), Mapper<DayStats> {

    override fun mapToDomain() = DayStats(documentId, time, day)
}

data class FirestoreWeekStats(
    var time: Long = 0,
    val day: Int = 0
) : Stats(), Mapper<WeekStats> {

    override fun mapToDomain() = WeekStats(documentId, time, day)
}

data class FirestoreMonthStats(
    var time: Long = 0,
    val day: Int = 0
) : Stats(), Mapper<MonthStats> {

    override fun mapToDomain() = MonthStats(documentId, time, day)
}
