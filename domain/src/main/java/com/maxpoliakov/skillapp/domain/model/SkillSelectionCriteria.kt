package com.maxpoliakov.skillapp.domain.model

sealed class SkillSelectionCriteria {
    object Any : SkillSelectionCriteria() {
        override suspend fun isValid(skill: Skill) = true
    }

    class WithId(private val id: Id) : SkillSelectionCriteria() {
        override suspend fun isValid(skill: Skill) = this.id == skill.id
    }

    class InGroupWithId(private val groupId: Id) : SkillSelectionCriteria() {
        override suspend fun isValid(skill: Skill) = skill.groupId == this.groupId
    }

    abstract suspend fun isValid(skill: Skill): Boolean
}
