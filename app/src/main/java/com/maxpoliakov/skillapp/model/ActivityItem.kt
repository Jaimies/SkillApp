package com.maxpoliakov.skillapp.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.maxpoliakov.skillapp.domain.model.Activity
import com.maxpoliakov.skillapp.domain.model.ActivityMinimal
import kotlinx.android.parcel.Parcelize
import java.time.Duration
import java.time.OffsetDateTime

@Keep
@Parcelize
data class ActivityItem(
    override val id: Int,
    override val name: String,
    override val totalTime: Duration,
    val lastWeekTime: Duration,
    val creationDate: OffsetDateTime,
    val parentActivity: ActivityMinimalItem?,
    val subActivities: List<ActivityMinimalItem>
) : Recordable, Parcelable

fun Activity.mapToPresentation() = ActivityItem(
    id, name, totalTime, lastWeekTime, timestamp,
    parentActivity?.mapToPresentation(), subActivities.map { it.mapToPresentation() }
)

fun ActivityItem.mapToDomain() = Activity(
    name,
    totalTime,
    parentActivity?.mapToDomain(),
    subActivities.map { it.mapToDomain() },
    lastWeekTime,
    id,
    creationDate
)

@Parcelize
class ActivityMinimalItem(
    override val id: Int,
    override val name: String,
    override val totalTime: Duration
) : Recordable, Parcelable

fun ActivityMinimal.mapToPresentation() = ActivityMinimalItem(id, name, totalTime)
fun ActivityMinimalItem.mapToDomain() = ActivityMinimal(id, name, totalTime)
