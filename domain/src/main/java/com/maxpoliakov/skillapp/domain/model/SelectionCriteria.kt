package com.maxpoliakov.skillapp.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

sealed class SelectionCriteria {
    object All : SelectionCriteria() {
        override suspend fun isIdValid(id: Id) = true
    }

    class Skill(private val id: Id) : SelectionCriteria() {
        override suspend fun isIdValid(id: Id) = this.id == id
    }

    class Group(group: Flow<SkillGroup>) : SelectionCriteria() {
        private val ids = group
            .map { group -> group.skills.map { skill -> skill.id } }
            .distinctUntilChanged()

        override suspend fun isIdValid(id: Id) = id in ids.first()
    }

    abstract suspend fun isIdValid(id: Id): Boolean
}
