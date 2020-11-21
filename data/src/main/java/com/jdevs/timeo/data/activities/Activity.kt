package com.jdevs.timeo.data.activities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdevs.timeo.domain.model.Activity
import java.time.OffsetDateTime

@Entity(tableName = "activities")
data class DBActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val totalTime: Int = 0,
    val lastWeekTime: Int = 0,
    val creationDate: OffsetDateTime = OffsetDateTime.now()
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
