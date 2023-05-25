package com.maxpoliakov.skillapp.data.timer

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.domain.model.Timer
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
    val groupId: Int,
    val startTime: ZonedDateTime,
)

fun Timer.mapToDB() = DBTimer(skillId, groupId, startTime)
fun DBTimer.mapToDomain() = Timer(skillId, groupId, startTime)
