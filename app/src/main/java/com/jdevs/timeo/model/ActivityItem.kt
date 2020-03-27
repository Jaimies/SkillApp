package com.jdevs.timeo.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.jdevs.timeo.domain.model.Activity
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Keep
@Parcelize
data class ActivityItem(
    override val id: String,
    val name: String,
    val totalTime: Int,
    val lastWeekTime: Int,
    val creationDate: OffsetDateTime,
    val parentActivityId: String
) : ViewItem, Parcelable

fun Activity.mapToPresentation() =
    ActivityItem(id, name, totalTime, lastWeekTime, creationDate, parentActivityId)

fun ActivityItem.mapToDomain() =
    Activity(id, name, totalTime, lastWeekTime, creationDate, parentActivityId)
