package com.jdevs.timeo.data

import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.util.time.toDate
import com.jdevs.timeo.util.time.toOffsetDate
import org.threeten.bp.OffsetDateTime
import java.util.Date

abstract class DataItem : ViewItem {

    abstract var firestoreTimestamp: Date?
    abstract var creationDate: OffsetDateTime

    fun setupFirestoreTimestamp() {

        firestoreTimestamp = creationDate.toDate()
    }

    fun setupTimestamp() {

        creationDate = firestoreTimestamp.toOffsetDate()
    }
}
