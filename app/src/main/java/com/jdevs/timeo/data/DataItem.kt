package com.jdevs.timeo.data

import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.util.toDate
import com.jdevs.timeo.util.toOffsetDate
import org.threeten.bp.OffsetDateTime
import java.util.Date

abstract class DataItem : ViewItem {

    abstract var documentId: String
    abstract var firestoreTimestamp: Date?
    abstract var creationDate: OffsetDateTime

    fun setupFirestoreTimestamp() {

        firestoreTimestamp = creationDate.toDate()
    }

    fun setupTimestamp() {

        creationDate = firestoreTimestamp.toOffsetDate()
    }
}
