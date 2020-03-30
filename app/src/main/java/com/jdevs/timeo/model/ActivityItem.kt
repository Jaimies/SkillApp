package com.jdevs.timeo.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.ActivityMinimal
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
    val parentActivity: ActivityMinimalItem?,
    val subActivities: List<ActivityMinimalItem>
) : ViewItem, Parcelable

fun Activity.mapToPresentation() = ActivityItem(
    id, name, totalTime, lastWeekTime, creationDate,
    parentActivity?.mapToPresentation(), subActivities.map { it.mapToPresentation() }
)

fun ActivityItem.mapToDomain() = Activity(
    id, name, totalTime, lastWeekTime, creationDate,
    parentActivity?.mapToDomain(), subActivities.map { it.mapToDomain() }
)

@Parcelize
class ActivityMinimalItem(
    override val id: String, val name: String, val totalTime: Int
) : ViewItem, Parcelable

fun ActivityMinimal.mapToPresentation() = ActivityMinimalItem(id, name, totalTime)
fun ActivityMinimalItem.mapToDomain() = ActivityMinimal(id, name, totalTime)
