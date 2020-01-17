package com.jdevs.timeo.data.firestore

import com.jdevs.timeo.util.time.WEEK_DAYS
import com.jdevs.timeo.util.time.getDaysAgo

abstract class Recordable {

    abstract val recentRecords: List<RecordMinimal>

    fun getLastWeekTime(): Int {

        return recentRecords
            .filter { it.creationDate.getDaysAgo() < WEEK_DAYS }
            .sumBy { it.time }
    }
}
