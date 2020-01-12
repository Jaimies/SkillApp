package com.jdevs.timeo.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.jdevs.timeo.data.activities.DBActivity
import com.jdevs.timeo.data.activities.FirestoreActivity
import com.jdevs.timeo.util.ViewTypes.ACTIVITY
import com.jdevs.timeo.util.time.toDate
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Keep
@Parcelize
data class Activity(
    override val id: Int = 0,
    override val documentId: String = "",
    val name: String,
    val totalTime: Long = 0,
    val lastWeekTime: Int = 0,
    override val creationDate: OffsetDateTime = OffsetDateTime.now()
) : Entity<DBActivity, FirestoreActivity>, DataItem, Parcelable {

    @IgnoredOnParcel
    override val viewType = ACTIVITY

    override fun toDB() = DBActivity(id, name, totalTime, lastWeekTime, creationDate)

    override fun toFirestore() =
        FirestoreActivity(documentId, name, totalTime, timestamp = creationDate.toDate())
}
