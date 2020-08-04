package com.jdevs.timeo.data.firestore

import androidx.annotation.Keep
import com.google.firebase.firestore.Exclude
import com.jdevs.timeo.shared.util.EPOCH
import com.jdevs.timeo.shared.util.daysSinceEpoch
import org.threeten.bp.OffsetDateTime

@Keep
data class RecordMinimal(val time: Int = 0, val day: Int = OffsetDateTime.now().daysSinceEpoch) {
    val creationDate: OffsetDateTime @Exclude get() = EPOCH.plusDays(day.toLong())
}
