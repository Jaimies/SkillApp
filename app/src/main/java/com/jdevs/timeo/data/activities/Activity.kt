package com.jdevs.timeo.data.activities


import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.data.firestore.RecordMinimal
import com.jdevs.timeo.data.firestore.Recordable
import com.jdevs.timeo.model.Activity
import com.jdevs.timeo.util.time.toOffsetDate
import org.threeten.bp.OffsetDateTime
import java.util.Date

@Keep
@Entity(tableName = "activities")
data class DBActivity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var totalTime: Long = 0,
    var lastWeekTime: Int = 0,
    var creationDate: OffsetDateTime = OffsetDateTime.now()
) : Mapper<Activity> {

    override fun mapToDomain() = Activity(
        id = id,
        name = name,
        totalTime = totalTime,
        lastWeekTime = lastWeekTime,
        creationDate = creationDate
    )
}

@Keep
data class FirestoreActivity(
    @DocumentId
    val documentId: String = "",
    val name: String = "",
    val totalTime: Long = 0,
    override var recentRecords: List<RecordMinimal> = emptyList(),
    @ServerTimestamp
    var timestamp: Date? = null
) : Recordable(), Mapper<Activity> {

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
}
