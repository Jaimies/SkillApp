package com.jdevs.timeo.data.db.model


import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdevs.timeo.data.Mapper
import com.jdevs.timeo.model.Activity
import org.threeten.bp.OffsetDateTime

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
