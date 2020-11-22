package com.jdevs.timeo.data.activities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.shared.util.getCurrentDateTime
import java.time.Duration
import java.time.OffsetDateTime

@Entity(tableName = "activities")
data class DBActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val totalTime: Duration = Duration.ZERO,
    val lastWeekTime: Duration = Duration.ZERO,
    val creationDate: OffsetDateTime = getCurrentDateTime()
)

fun DBActivity.mapToDomain(): Activity {
    return Activity(
        name,
        totalTime,
        null,
        listOf(),
        lastWeekTime,
        id,
        creationDate
    )
}

fun Activity.mapToDB(): DBActivity {
    return DBActivity(
        id,
        name,
        totalTime,
        lastWeekTime,
        timestamp)
}
