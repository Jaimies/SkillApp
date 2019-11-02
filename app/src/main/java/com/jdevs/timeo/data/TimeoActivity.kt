package com.jdevs.timeo.data

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

// TimeoActivity (task) class
@Keep
@Parcelize
data class TimeoActivity(
    val title: String = "",
    val icon: String = "",
    var totalTime: Int = -1,
    @ServerTimestamp var timestamp: Date = Calendar.getInstance().time
) : Parcelable {

    constructor() : this("", "") {

        timestamp = Calendar.getInstance().time

    }

}