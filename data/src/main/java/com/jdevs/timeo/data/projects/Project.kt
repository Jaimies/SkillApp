package com.jdevs.timeo.data.projects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdevs.timeo.domain.model.Project
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "projects")
data class DBProject(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val totalTime: Int = 0,
    val lastWeekTime: Int = 0,
    val creationDate: OffsetDateTime = OffsetDateTime.now()
)

fun Project.mapToDB() =
    DBProject(id, name, description, totalTime, lastWeekTime, creationDate)

fun DBProject.mapToDomain() =
    Project(id, name, description, totalTime, lastWeekTime, creationDate)
