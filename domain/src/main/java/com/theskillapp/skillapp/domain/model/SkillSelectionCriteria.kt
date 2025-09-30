package com.theskillapp.skillapp.domain.model

// todo we can do better naming of subclasses
sealed class SkillSelectionCriteria {
    object Any : SkillSelectionCriteria() {
        override fun isSatisfiedBy(skill: Skill) = true
    }

    class WithId(private val id: Id) : SkillSelectionCriteria() {
        override fun isSatisfiedBy(skill: Skill) = this.id == skill.id
    }

    class WithIdInList(private val ids: List<Id>): SkillSelectionCriteria() {
        override fun isSatisfiedBy(skill: Skill) = skill.id in this.ids
    }

    class InGroupWithId(private val groupId: Id) : SkillSelectionCriteria() {
        override fun isSatisfiedBy(skill: Skill) = skill.groupId == this.groupId
    }

    object NotInAGroup: SkillSelectionCriteria() {
        override fun isSatisfiedBy(skill: Skill) = skill.isNotInAGroup
    }

    class WithUnit(private val unit: MeasurementUnit<*>) : SkillSelectionCriteria() {
        override fun isSatisfiedBy(skill: Skill) = skill.unit == unit
    }

    class Combined(
        private vararg val criteria: SkillSelectionCriteria,
    ) : SkillSelectionCriteria() {
        override fun isSatisfiedBy(skill: Skill): Boolean {
            return criteria.all { it.isSatisfiedBy(skill) }
        }
    }

    fun and(criteria: SkillSelectionCriteria): SkillSelectionCriteria {
        return Combined(this, criteria)
    }

    fun withUnit(unit: MeasurementUnit<*>): SkillSelectionCriteria {
        return this.and(WithUnit(unit))
    }

    abstract fun isSatisfiedBy(skill: Skill): Boolean
}
