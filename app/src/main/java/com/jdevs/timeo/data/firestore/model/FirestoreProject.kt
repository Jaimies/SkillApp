package com.jdevs.timeo.data.firestore.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.data.firestore.RecordMinimal
import com.jdevs.timeo.model.Project
import com.jdevs.timeo.util.time.toOffsetDate
import java.util.Date

data class FirestoreProject(
    @DocumentId
    val documentId: String = "",
    val name: String = "",
    val totalTime: Long = 0,
    override var recentRecords: List<RecordMinimal> = emptyList(),
    @ServerTimestamp
    var timestamp: Date? = null
) : Recordable(), Mapper<Project> {

    override fun mapToDomain(): Project {

        val lastWeekTime = getLastWeekTime()
        return Project(
            documentId = documentId,
            name = name,
            totalTime = totalTime,
            lastWeekTime = lastWeekTime,
            creationDate = timestamp.toOffsetDate()
        )
    }
}
