package com.jdevs.timeo.data.firestore.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.data.firestore.RecordMinimal
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.util.Time.WEEK_DAYS
import com.jdevs.timeo.util.time.getDaysAgo
import com.jdevs.timeo.util.time.toOffsetDate
import java.util.Date

data class FirestoreActivity(
    @DocumentId
    val documentId: String = "",
    val name: String = "",
    val totalTime: Long = 0,
    var recentRecords: List<RecordMinimal> = emptyList(),
    @ServerTimestamp
    var timestamp: Date? = null
) : Mapper<Activity> {

    override fun mapToDomain(): Activity {

        val lastWeekTime = getLastWeekTime()
        return Activity(
            documentId = documentId,
            name = name,
            totalTime = totalTime,
            lastWeekTime = lastWeekTime,
            creationDate = timestamp.toOffsetDate()
        )
    }

    private fun getLastWeekTime(): Int {

        return recentRecords
            .filter { it.creationDate.getDaysAgo() < WEEK_DAYS }
            .sumBy { it.time }
    }
}
