package com.maxpoliakov.skillapp.ui.skills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent

class SkillViewModel {
    val skill: LiveData<Skill> get() = _skill
    private val _skill = MutableLiveData<Skill>()
    val state = skill.map { it.toSkillState() }

    fun setSkill(value: Skill) {
        _skill.value = value
    }

    val navigateToDetails = SingleLiveEvent<Any>()
    fun navigateToDetails() = navigateToDetails.call()
}
