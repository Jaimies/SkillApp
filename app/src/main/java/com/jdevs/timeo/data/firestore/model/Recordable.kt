package com.jdevs.timeo.data.firestore.model

import com.jdevs.timeo.data.firestore.RecordMinimal
import com.jdevs.timeo.util.time.WEEK_DAYS
import com.jdevs.timeo.util.time.getDaysAgo

abstract class Recordable {

    abstract val recentRecords: List<RecordMinimal>

    protected fun getLastWeekTime(): Int {

        return recentRecords
            .filter { it.creationDate.getDaysAgo() < WEEK_DAYS }
            .sumBy { it.time }
    }
}
