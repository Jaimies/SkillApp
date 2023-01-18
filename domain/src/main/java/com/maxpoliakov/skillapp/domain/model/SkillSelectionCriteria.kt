package com.maxpoliakov.skillapp.domain.model

// todo we can do better naming of subclasses
sealed class SkillSelectionCriteria {
    object Any : SkillSelectionCriteria() {
        override fun isValid(skill: Skill) = true
    }

    class WithId(private val id: Id) : SkillSelectionCriteria() {
        override fun isValid(skill: Skill) = this.id == skill.id
    }

    class WithIdInList(private val ids: List<Id>): SkillSelectionCriteria() {
        override fun isValid(skill: Skill) = skill.id in this.ids
    }

    class InGroupWithId(private val groupId: Id) : SkillSelectionCriteria() {
        override fun isValid(skill: Skill) = skill.groupId == this.groupId
    }

    class WithUnit(private val unit: MeasurementUnit<*>) : SkillSelectionCriteria() {
        override fun isValid(skill: Skill) = skill.unit == unit
    }

    class Combined(
        private vararg val criteria: SkillSelectionCriteria,
    ) : SkillSelectionCriteria() {
        override fun isValid(skill: Skill): Boolean {
            return criteria.all { it.isValid(skill) }
        }
    }

    fun and(criteria: SkillSelectionCriteria): SkillSelectionCriteria {
        return Combined(this, criteria)
    }

    fun withUnit(unit: MeasurementUnit<*>): SkillSelectionCriteria {
        return this.and(WithUnit(unit))
    }

    abstract fun isValid(skill: Skill): Boolean
}
