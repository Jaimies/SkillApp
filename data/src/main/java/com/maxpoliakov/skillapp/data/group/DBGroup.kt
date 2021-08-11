package com.maxpoliakov.skillapp.data.group

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.skill.mapToDomain
import com.maxpoliakov.skillapp.domain.model.SkillGroup

@Entity(tableName = "groups")
data class DBGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
)

data class GroupWithSkills(
    @Embedded val group: DBGroup,
    @Relation(
        parentColumn = "id",
        entityColumn = "groupId"
    )
    val skills: List<DBSkill>
)

fun GroupWithSkills.mapToDomain() = SkillGroup(
    group.id,
    group.name,
    skills.map(DBSkill::mapToDomain)
)
