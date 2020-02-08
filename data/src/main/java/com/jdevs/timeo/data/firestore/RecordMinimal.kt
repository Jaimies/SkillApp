package com.jdevs.timeo.data.firestore

import com.google.firebase.firestore.Exclude
import com.jdevs.timeo.shared.time.toOffsetDate
import java.util.Calendar
import java.util.Date

data class RecordMinimal(
    var time: Int = 0,
    var date: Date = Calendar.getInstance().time
) {

    @get:Exclude
    val creationDate
        get() = date.toOffsetDate()
}
