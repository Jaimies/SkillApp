package com.jdevs.timeo.ui.model

import android.os.Parcelable
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.ui.model.ViewType.PROJECT
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Parcelize
data class ProjectItem(
    override val id: Int = 0,
    override val documentId: String = "",
    val name: String,
    val totalTime: Long = 0,
    val lastWeekTime: Int = 0,
    val creationDate: OffsetDateTime = OffsetDateTime.now()
) : ViewItem, Parcelable {

    @IgnoredOnParcel
    override val viewType = PROJECT
}

fun Project.mapToPresentation() =
    ProjectItem(id, documentId, name, totalTime, lastWeekTime, creationDate)

fun ProjectItem.mapToDomain() = Project(id, documentId, name, totalTime, lastWeekTime, creationDate)
