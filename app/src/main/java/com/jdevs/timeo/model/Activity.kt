package com.jdevs.timeo.model

import android.os.Parcelable
import com.jdevs.timeo.data.db.model.DBActivity
import com.jdevs.timeo.data.firestore.model.FirestoreActivity
import com.jdevs.timeo.util.AdapterConstants.ACTIVITY
import com.jdevs.timeo.util.time.toDate
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Parcelize
data class Activity(
    override val id: Int = 0,
    override val documentId: String = "",
    val name: String,
    val totalTime: Long = 0,
    val lastWeekTime: Int = 0,
    override val creationDate: OffsetDateTime = OffsetDateTime.now()
) : Parcelable, DataItem() {

    @IgnoredOnParcel
    override val viewType = ACTIVITY

    fun toDBActivity() = DBActivity(id, name, totalTime, lastWeekTime, creationDate)

    fun toFirestoreActivity() =
        FirestoreActivity(documentId, name, totalTime, timestamp = creationDate.toDate())
}
