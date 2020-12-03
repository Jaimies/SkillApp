package com.maxpoliakov.skillapp.data.skill

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.Duration
import java.time.LocalDate

@Entity(tableName = "skills")
data class DBSkill(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val totalTime: Duration = Duration.ZERO,
    val lastWeekTime: Duration = Duration.ZERO,
    val creationDate: LocalDate = getCurrentDate()
)

fun DBSkill.mapToDomain(): Skill {
    return Skill(
        name,
        totalTime,
        lastWeekTime,
        id,
        creationDate
    )
}

fun Skill.mapToDB(): DBSkill {
    return DBSkill(
        id,
        name,
        totalTime,
        lastWeekTime,
        date)
}
