package com.jdevs.timeo.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.jdevs.timeo.domain.model.Project
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Keep
@Parcelize
data class ProjectItem(
    override val id: String,
    val name: String,
    val description: String,
    val totalTime: Int,
    val lastWeekTime: Int,
    val creationDate: OffsetDateTime
) : ViewItem, Parcelable

fun Project.mapToPresentation() =
    ProjectItem(id, name, description, totalTime, lastWeekTime, creationDate)

fun ProjectItem.mapToDomain() =
    Project(id, name, description, totalTime, lastWeekTime, creationDate)
