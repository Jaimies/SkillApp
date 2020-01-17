package com.jdevs.timeo.data.stats

import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.Stats
import com.jdevs.timeo.domain.model.WeekStats
import javax.inject.Inject


class DBDayStatsMapper @Inject constructor() : Mapper<DBDayStats, Stats> {

    override fun map(input: DBDayStats) = input.run { DayStats(time = time, day = day) }
}

class DBWeekStatsMapper @Inject constructor() : Mapper<DBWeekStats, Stats> {

    override fun map(input: DBWeekStats) = input.run { WeekStats(time = time, week = week) }
}

class DBMonthStatsMapper @Inject constructor() : Mapper<DBMonthStats, Stats> {

    override fun map(input: DBMonthStats) = input.run { MonthStats(time = time, month = month) }
}

class FirestoreStatsMapper @Inject constructor() : Mapper<FirestoreDayStats, Stats> {

    override fun map(input: FirestoreDayStats) = input.run { DayStats(documentId, time, day) }
}

class FirestoreWeekStatsMapper @Inject constructor() : Mapper<FirestoreWeekStats, WeekStats> {

    override fun map(input: FirestoreWeekStats) = input.run { WeekStats(documentId, time, day) }
}

class FirestoreMonthStatsMapper @Inject constructor() : Mapper<FirestoreMonthStats, MonthStats> {

    override fun map(input: FirestoreMonthStats) = input.run { MonthStats(documentId, time, day) }
}
