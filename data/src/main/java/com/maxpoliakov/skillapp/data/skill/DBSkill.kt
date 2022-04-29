@file:UseSerializers(DurationAsLongSerializer::class, LocalDateAsStringSerializer::class)

package com.maxpoliakov.skillapp.data.skill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxpoliakov.skillapp.data.parseGoal
import com.maxpoliakov.skillapp.data.serialization.DurationAsLongSerializer
import com.maxpoliakov.skillapp.data.serialization.LocalDateAsStringSerializer
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
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
    val unit: MeasurementUnit = MeasurementUnit.Millis,
    @ColumnInfo(name = "totalTime")
    val totalCount: Long = 0,

    @ColumnInfo(name = "initialTime")
    val initialCount: Long = 0,
    @Transient
    @ColumnInfo(name = "lastWeekTime")
    val lastWeekCount: Long = 0,
    val creationDate: LocalDate = getCurrentDate(),
    val groupId: Int = -1,
    val goalType: Goal.Type = Goal.Type.Daily,
    @ColumnInfo(name = "goalTime")
    val goalCount: Long = 0,
    val order: Int = -1,
)

fun DBSkill.mapToDomain(): Skill {
    return Skill(
        name,
        unit,
        totalCount,
        initialCount,
        lastWeekCount,
        id,
        creationDate,
        groupId,
        parseGoal(goalCount, goalType),
        order,
    )
}

fun Skill.mapToDB(): DBSkill {
    return DBSkill(
        id,
        name,
        unit,
        totalCount,
        initialCount,
        lastWeekCount,
        date,
        groupId,
        goal?.type ?: Goal.Type.Daily,
        goal?.count ?: 0,
        order,
    )
}
