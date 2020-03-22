package com.jdevs.timeo.data.firestore

import com.google.firebase.firestore.Exclude
import com.jdevs.timeo.shared.util.WEEK_DAYS
import com.jdevs.timeo.shared.util.daysAgo

abstract class Recordable {

    abstract val recentRecords: List<RecordMinimal>

    val lastWeekTime
        @Exclude get() = recentRecords
            .filter { it.creationDate.daysAgo < WEEK_DAYS }
            .sumBy { it.time }
}
