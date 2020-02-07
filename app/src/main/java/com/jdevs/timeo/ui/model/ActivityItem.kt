package com.jdevs.timeo.ui.model

import android.os.Parcelable
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.ui.model.ViewType.ACTIVITY
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Parcelize
data class ActivityItem(
    override val id: Int = 0,
    override val documentId: String = "",
    val name: String,
    val totalTime: Long = 0,
    val lastWeekTime: Int = 0,
    val creationDate: OffsetDateTime = OffsetDateTime.now()
) : ViewItem, Parcelable {

    @IgnoredOnParcel
    override val viewType = ACTIVITY
}

fun Activity.mapToPresentation() =
    ActivityItem(id, documentId, name, totalTime, lastWeekTime, creationDate)

fun ActivityItem.mapToDomain() =
    Activity(id, documentId, name, totalTime, lastWeekTime, creationDate)
