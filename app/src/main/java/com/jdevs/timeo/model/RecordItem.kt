package com.jdevs.timeo.model

import androidx.annotation.Keep
import com.jdevs.timeo.domain.model.Record
import org.threeten.bp.OffsetDateTime

@Keep
data class RecordItem(
    override val id: Int,
    val name: String,
    val time: Int,
    val activityId: Int,
    val timestamp: OffsetDateTime
) : ViewItem

fun Record.mapToPresentation() = RecordItem(id, name, time, activityId, timestamp)

fun RecordItem.mapToDomain() = Record(name, time, activityId, id, timestamp)
