package com.jdevs.timeo.model

import androidx.annotation.Keep
import com.jdevs.timeo.domain.model.Record
import org.threeten.bp.OffsetDateTime

@Keep
data class RecordItem(
    override val id: String,
    val name: String,
    val time: Int,
    val activityId: String,
    val creationDate: OffsetDateTime
) : ViewItem

fun Record.mapToPresentation() = RecordItem(id, name, time, activityId, creationDate)

fun RecordItem.mapToDomain() = Record(id, name, time, activityId, creationDate)
