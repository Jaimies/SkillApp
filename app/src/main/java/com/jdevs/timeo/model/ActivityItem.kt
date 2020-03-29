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
    val parentActivityName: String,
    val parentActivityId: String,
    val subActivities: List<ActivityMinimalItem>
) : ViewItem, Parcelable

fun Activity.mapToPresentation() = ActivityItem(
    id, name, totalTime, lastWeekTime, creationDate,
    parentActivityName, parentActivityId, subActivities.map { it.mapToPresentation() }
)

fun ActivityItem.mapToDomain() = Activity(
    id, name, totalTime, lastWeekTime, creationDate,
    parentActivityName, parentActivityId, subActivities.map { it.mapToDomain() }
)

@Parcelize
data class ActivityMinimalItem(val name: String, val totalTime: Int) : Parcelable

fun ActivityMinimal.mapToPresentation() = ActivityMinimalItem(name, totalTime)
fun ActivityMinimalItem.mapToDomain() = ActivityMinimal(name, totalTime)
