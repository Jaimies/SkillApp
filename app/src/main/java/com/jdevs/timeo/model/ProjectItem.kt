package com.jdevs.timeo.model

import android.os.Parcelable
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.model.ViewType.PROJECT
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Parcelize
data class ProjectItem(
    override val id: Int,
    override val documentId: String,
    val name: String,
    val totalTime: Long,
    val lastWeekTime: Int,
    val creationDate: OffsetDateTime
) : ViewItem, Parcelable {

    @IgnoredOnParcel
    override val viewType = PROJECT
}

fun Project.mapToPresentation() =
    ProjectItem(id, documentId, name, totalTime, lastWeekTime, creationDate)

fun ProjectItem.mapToDomain() = Project(id, documentId, name, totalTime, lastWeekTime, creationDate)
