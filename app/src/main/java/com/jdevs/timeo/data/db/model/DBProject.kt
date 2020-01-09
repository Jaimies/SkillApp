package com.jdevs.timeo.data.db.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.model.Project
import org.threeten.bp.OffsetDateTime

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
