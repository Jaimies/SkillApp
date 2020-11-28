package com.maxpoliakov.skillapp.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.maxpoliakov.skillapp.domain.model.Activity
import kotlinx.android.parcel.Parcelize
import java.time.Duration
import java.time.LocalDateTime

@Keep
@Parcelize
data class ActivityItem(
    override val id: Int,
    override val name: String,
    override val totalTime: Duration,
    val lastWeekTime: Duration,
    val creationDate: LocalDateTime
) : Recordable, Parcelable

fun Activity.mapToPresentation() = ActivityItem(
    id, name, totalTime, lastWeekTime, timestamp
)

fun ActivityItem.mapToDomain() = Activity(
    name,
    totalTime,
    lastWeekTime,
    id,
    creationDate
)
