package com.maxpoliakov.skillapp.domain.model

sealed class SelectionCriteria {
    object All : SelectionCriteria() {
        override suspend fun isValid(skill: com.maxpoliakov.skillapp.domain.model.Skill) = true
    }

    class Skill(private val id: Id) : SelectionCriteria() {
        override suspend fun isValid(skill: com.maxpoliakov.skillapp.domain.model.Skill) = this.id == skill.id
    }

    class Group(private val groupId: Id) : SelectionCriteria() {
        override suspend fun isValid(skill: com.maxpoliakov.skillapp.domain.model.Skill) = skill.groupId == this.groupId
    }

    abstract suspend fun isValid(skill: com.maxpoliakov.skillapp.domain.model.Skill): Boolean
}
