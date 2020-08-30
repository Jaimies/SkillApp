package com.jdevs.timeo.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.jdevs.timeo.domain.model.Project
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Keep
@Parcelize
data class ProjectItem(
    override val id: Int,
    override val name: String,
    val description: String,
    override val totalTime: Int,
    val lastWeekTime: Int,
    val creationDate: OffsetDateTime
) : Recordable, Parcelable

fun Project.mapToPresentation() =
    ProjectItem(id, name, description, totalTime, lastWeekTime, creationDate)

fun ProjectItem.mapToDomain() =
    Project(id, name, description, totalTime, lastWeekTime, creationDate)
