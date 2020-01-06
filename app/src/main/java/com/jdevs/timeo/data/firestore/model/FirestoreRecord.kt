package com.jdevs.timeo.data.firestore.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.util.time.toOffsetDate
import java.util.Date

data class FirestoreRecord(
    @DocumentId
    var documentId: String,
    var name: String,
    var time: Long,
    var activityId: String,
    @ServerTimestamp
    var timestamp: Date
) : Mapper<Record> {

    override fun mapToDomain() = Record(
        documentId = documentId,
        name = name,
        time = time,
        activityId = activityId,
        creationDate = timestamp.toOffsetDate()
    )
}
