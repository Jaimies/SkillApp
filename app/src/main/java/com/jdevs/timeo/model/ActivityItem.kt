package com.jdevs.timeo.model

import android.os.Parcelable
import com.jdevs.timeo.domain.model.Activity
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Parcelize
data class ActivityItem(
    override val id: String,
    val name: String,
    val totalTime: Long,
    val lastWeekTime: Int,
    val creationDate: OffsetDateTime
) : ViewItem, Parcelable

fun Activity.mapToPresentation() = ActivityItem(id, name, totalTime, lastWeekTime, creationDate)

fun ActivityItem.mapToDomain() = Activity(id, name, totalTime, lastWeekTime, creationDate)
