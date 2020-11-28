package com.maxpoliakov.skillapp.model

import androidx.annotation.Keep
import com.maxpoliakov.skillapp.domain.model.Record
import java.time.Duration
import java.time.LocalDateTime

@Keep
data class RecordItem(
    override val id: Int,
    val name: String,
    val time: Duration,
    val skillId: Int,
    val timestamp: LocalDateTime
) : ViewItem

fun Record.mapToPresentation() = RecordItem(id, name, time, skillId, timestamp)
fun RecordItem.mapToDomain() = Record(name, skillId, time, id, timestamp)
