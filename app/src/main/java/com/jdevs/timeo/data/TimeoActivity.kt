package com.jdevs.timeo.data

import androidx.annotation.Keep
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

// TimeoActivity (task) class
@Keep
data class TimeoActivity(
    val title: String = "",
    val icon: String = "",
    @ServerTimestamp var timestamp: Date = Calendar.getInstance().time
) {

    constructor() : this("", "") {

        timestamp = Calendar.getInstance().time

    }

}