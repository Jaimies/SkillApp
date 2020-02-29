package com.jdevs.timeo.data.firestore

import androidx.annotation.Keep
import com.google.firebase.firestore.Exclude
import com.jdevs.timeo.shared.time.toOffsetDate
import java.util.Calendar
import java.util.Date

@Keep
data class RecordMinimal(val time: Int = 0, val date: Date = Calendar.getInstance().time) {

    val creationDate @Exclude get() = date.toOffsetDate()
}
