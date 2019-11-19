package com.jdevs.timeo.data

import androidx.annotation.Keep
import com.google.firebase.firestore.ServerTimestamp
import java.util.Calendar
import java.util.Date

@Keep
data class Record(
    val title: String,
    val workingTime: Int = 0,
    val activityId: String = "",
    @ServerTimestamp var timestamp: Date = Calendar.getInstance().time
) {
    constructor() : this("", 0, "") {
        timestamp = Calendar.getInstance().time
    }
}
