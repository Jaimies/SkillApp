package com.theskillapp.skillapp.data.timer

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.theskillapp.skillapp.data.skill.DBSkill
import com.theskillapp.skillapp.domain.model.Timer
import java.time.ZonedDateTime

@Entity(
    tableName = "timers",
    foreignKeys = [
        ForeignKey(
            entity = DBSkill::class,
            parentColumns = ["id"],
            childColumns = ["skillId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ]
)
data class DBTimer(
    @PrimaryKey
    val skillId: Int,
    val startTime: ZonedDateTime,
)

fun Timer.mapToDB() = DBTimer(skillId, startTime)
fun DBTimer.mapToDomain() = Timer(skillId, startTime)
