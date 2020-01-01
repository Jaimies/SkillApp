package com.jdevs.timeo.data

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.jdevs.timeo.util.toOffsetDate
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime
import java.util.Calendar
import java.util.Date

@Parcelize
data class RecordMinimal(
    var time: Long = 0,
    var date: Date = Calendar.getInstance().time,
    @get:Exclude
    var creationDate: OffsetDateTime = OffsetDateTime.now()
) : Parcelable {

    fun setupTimestamp() {

        creationDate = date.toOffsetDate()
    }
}
