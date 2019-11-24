package com.jdevs.timeo.data

import androidx.annotation.Keep
import com.google.firebase.firestore.ServerTimestamp
import java.util.Calendar
import java.util.Date

@Keep
data class Record(
    val name: String = "",
    val time: Long = 0,
    val activityId: String = "",
    @ServerTimestamp var timestamp: Date = Calendar.getInstance().time
)
