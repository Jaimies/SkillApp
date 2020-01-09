package com.jdevs.timeo.model

import android.os.Parcelable
import com.jdevs.timeo.data.db.model.DBProject
import com.jdevs.timeo.data.firestore.model.FirestoreProject
import com.jdevs.timeo.util.ViewTypes.PROJECT
import com.jdevs.timeo.util.time.toDate
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Parcelize
data class Project(
    override val id: Int = 0,
    override val documentId: String = "",
    val name: String,
    val totalTime: Long = 0,
    val lastWeekTime: Int = 0,
    override val creationDate: OffsetDateTime = OffsetDateTime.now()
) : Entity<DBProject, FirestoreProject>, DataItem, Parcelable {

    @IgnoredOnParcel
    override val viewType = PROJECT

    override fun toDB() = DBProject(id, name, totalTime, lastWeekTime, creationDate)

    override fun toFirestore() =
        FirestoreProject(documentId, name, totalTime, timestamp = creationDate.toDate())
}
