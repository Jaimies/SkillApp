@file:UseSerializers(DurationAsLongSerializer::class)

package com.maxpoliakov.skillapp.data.group

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.maxpoliakov.skillapp.data.parseGoal
import com.maxpoliakov.skillapp.data.serialization.DurationAsLongSerializer
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.skill.mapToDomain
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Duration

@Serializable
@Entity(tableName = "groups")
data class DBGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val goalType: Goal.Type = Goal.Type.Daily,
    val goalTime: Duration = Duration.ZERO,
    val order: Int = -1,
)

data class GroupWithSkills(
    @Embedded val group: DBGroup,
    @Relation(
        parentColumn = "id",
        entityColumn = "groupId",
    )
    val skills: List<DBSkill>
)

fun GroupWithSkills.mapToDomain() = SkillGroup(
    group.id,
    group.name,
    skills.map(DBSkill::mapToDomain),
    parseGoal(group.goalTime, group.goalType),
    group.order,
)
