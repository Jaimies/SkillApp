package com.jdevs.timeo.model

import android.os.Parcelable
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.model.ViewType.ACTIVITY
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Parcelize
data class ActivityItem(
    override val id: Int,
    override val documentId: String,
    val name: String,
    val totalTime: Long,
    val lastWeekTime: Int,
    val creationDate: OffsetDateTime
) : ViewItem, Parcelable {

    @IgnoredOnParcel
    override val viewType = ACTIVITY
}

fun Activity.mapToPresentation() =
    ActivityItem(id, documentId, name, totalTime, lastWeekTime, creationDate)

fun ActivityItem.mapToDomain() =
    Activity(id, documentId, name, totalTime, lastWeekTime, creationDate)
