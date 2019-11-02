package com.jdevs.timeo.data

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Keep
@Parcelize
data class TimeoRecord(
    val title: String,
    val workingTime: Int = 0,
    val activityId: String = "",
    @ServerTimestamp var timestamp: Date = Calendar.getInstance().time
) : Parcelable {

    constructor() : this("", 0, "") {

        timestamp = Calendar.getInstance().time

    }

}