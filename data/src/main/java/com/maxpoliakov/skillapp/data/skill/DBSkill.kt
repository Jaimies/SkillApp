@file:UseSerializers(LocalDateAsStringSerializer::class)

package com.maxpoliakov.skillapp.data.skill

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxpoliakov.skillapp.data.parseGoal
import com.maxpoliakov.skillapp.data.serialization.DBMeasurementUnit
import com.maxpoliakov.skillapp.data.serialization.DBMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.data.serialization.LocalDateAsStringSerializer
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

@Serializable
@Entity(tableName = "skills")
data class DBSkill(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val unit: DBMeasurementUnit = DBMeasurementUnit.Millis,
    val totalTime: Long = 0,
    val initialTime: Long = 0,
    @Transient
    val lastWeekTime: Long = 0,
    val creationDate: LocalDate = getCurrentDate(),
    val groupId: Int = -1,
    val goalType: Goal.Type = Goal.Type.Daily,
    val goalTime: Long = 0,
    val order: Int = -1,
)

fun DBSkill.mapToDomain(): Skill {
    return Skill(
        name,
        unit.toDomain(),
        totalTime,
        initialTime,
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
        unit.mapToUI(),
        totalCount,
        initialCount,
        0,
        date,
        groupId,
        goal?.type ?: Goal.Type.Daily,
        goal?.count ?: 0,
        order,
    )
}
