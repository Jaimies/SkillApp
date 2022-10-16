package com.maxpoliakov.skillapp.domain.model

sealed class SkillSelectionCriteria {
    object Any : SkillSelectionCriteria() {
        override fun isValid(skill: Skill) = true
    }

    class WithId(private val id: Id) : SkillSelectionCriteria() {
        override fun isValid(skill: Skill) = this.id == skill.id
    }

    class InGroupWithId(private val groupId: Id) : SkillSelectionCriteria() {
        override fun isValid(skill: Skill) = skill.groupId == this.groupId
    }

    abstract fun isValid(skill: Skill): Boolean
}
