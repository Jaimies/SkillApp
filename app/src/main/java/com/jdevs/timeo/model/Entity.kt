package com.jdevs.timeo.model

import com.jdevs.timeo.common.adapter.ViewItem
import org.threeten.bp.OffsetDateTime

interface Entity<DB, Firestore> {

    fun toDB(): DB
    fun toFirestore(): Firestore
}

interface DataItem : ViewItem {

    val creationDate: OffsetDateTime
}
