package com.jdevs.timeo.ui.model

import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.ui.model.ViewType.RECORD
import org.threeten.bp.OffsetDateTime

data class RecordItem(
    override val id: Int = 0,
    override val documentId: String = "",
    val name: String,
    val time: Long,
    val activityId: String = "",
    val roomActivityId: Int = 0,
    val creationDate: OffsetDateTime = OffsetDateTime.now()
) : ViewItem {

    override val viewType = RECORD
}

fun Record.mapToPresentation() =
    RecordItem(id, documentId, name, time, activityId, roomActivityId, creationDate)

fun RecordItem.mapToDomain() =
    Record(id, documentId, name, time, activityId, roomActivityId, creationDate)
