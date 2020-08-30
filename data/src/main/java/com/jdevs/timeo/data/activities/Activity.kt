package com.jdevs.timeo.data.activities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdevs.timeo.domain.model.Activity
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "activities")
data class DBActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val totalTime: Int = 0,
    val lastWeekTime: Int = 0,
    val creationDate: OffsetDateTime = OffsetDateTime.now()
)

fun DBActivity.mapToDomain() =
    Activity(id, name, totalTime, lastWeekTime, creationDate, null)

fun Activity.mapToDB() =
    DBActivity(id, name, totalTime, lastWeekTime, creationDate)