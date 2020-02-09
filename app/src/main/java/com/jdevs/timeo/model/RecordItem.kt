package com.jdevs.timeo.model

import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.model.ViewType.RECORD
import org.threeten.bp.OffsetDateTime

data class RecordItem(
    override val id: String,
    val name: String,
    val time: Long,
    val activityId: String,
    val creationDate: OffsetDateTime
) : ViewItem {

    override val viewType = RECORD
}

fun Record.mapToPresentation() = RecordItem(id, name, time, activityId, creationDate)

fun RecordItem.mapToDomain() = Record(id, name, time, activityId, creationDate)
