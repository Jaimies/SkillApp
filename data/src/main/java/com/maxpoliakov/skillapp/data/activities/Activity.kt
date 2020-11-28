package com.maxpoliakov.skillapp.data.activities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxpoliakov.skillapp.domain.model.Activity
import com.maxpoliakov.skillapp.shared.util.getCurrentDateTime
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
