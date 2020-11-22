package com.jdevs.timeo.model

import androidx.annotation.Keep
import com.jdevs.timeo.domain.model.Record
import java.time.Duration
import java.time.OffsetDateTime

@Keep
data class RecordItem(
    override val id: Int,
    val name: String,
    val time: Duration,
    val activityId: Int,
    val timestamp: OffsetDateTime
) : ViewItem

fun Record.mapToPresentation() = RecordItem(id, name, time, activityId, timestamp)
fun RecordItem.mapToDomain() = Record(name, activityId, time, id, timestamp)
