package com.jdevs.timeo.data.projects

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.data.firestore.RecordMinimal
import com.jdevs.timeo.data.firestore.Recordable
import com.jdevs.timeo.model.Project
import com.jdevs.timeo.util.time.toOffsetDate
import org.threeten.bp.OffsetDateTime
import java.util.Date

@Keep
@Entity(tableName = "projects")
data class DBProject(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var totalTime: Long = 0,
    var lastWeekTime: Int = 0,
    var creationDate: OffsetDateTime = OffsetDateTime.now()
) : Mapper<Project> {

    override fun mapToDomain() =
        Project(
            id = id,
            name = name,
            totalTime = totalTime,
            lastWeekTime = lastWeekTime,
            creationDate = creationDate
        )
}

@Keep
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
