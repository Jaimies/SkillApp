package com.jdevs.timeo.domain.model

import com.jdevs.timeo.ui.common.adapter.ViewItem
import org.threeten.bp.OffsetDateTime

interface Entity<DB, Firestore> {

    fun toDB(): DB
    fun toFirestore(): Firestore
}

interface DataItem : ViewItem {

    val creationDate: OffsetDateTime
}
