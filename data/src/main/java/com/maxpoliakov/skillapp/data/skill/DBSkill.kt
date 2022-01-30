@file:UseSerializers(DurationAsLongSerializer::class, LocalDateAsStringSerializer::class)

package com.maxpoliakov.skillapp.data.skill

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxpoliakov.skillapp.data.parseGoal
import com.maxpoliakov.skillapp.data.serialization.DurationAsLongSerializer
import com.maxpoliakov.skillapp.data.serialization.LocalDateAsStringSerializer
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import java.time.Duration
import java.time.LocalDate

@Serializable
@Entity(tableName = "skills")
data class DBSkill(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val totalTime: Duration = Duration.ZERO,
    val initialTime: Duration = Duration.ZERO,
    @Transient
    val lastWeekTime: Duration = Duration.ZERO,
    val creationDate: LocalDate = getCurrentDate(),
    val groupId: Int = -1,
    val goalType: Goal.Type = Goal.Type.Daily,
    val goalTime: Duration = Duration.ZERO,
    val order: Int = -1,
)

fun DBSkill.mapToDomain(): Skill {
    return Skill(
        name,
        totalTime,
        initialTime,
        lastWeekTime,
        id,
        creationDate,
        groupId,
        parseGoal(goalTime, goalType),
        order,
    )
}

fun Skill.mapToDB(): DBSkill {
    return DBSkill(
        id,
        name,
        totalTime,
        initialTime,
        lastWeekTime,
        date,
        groupId,
        goal?.type ?: Goal.Type.Daily,
        goal?.time ?: Duration.ZERO,
        order,
    )
}
