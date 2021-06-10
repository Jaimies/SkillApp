package com.maxpoliakov.skillapp.ui.skills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
import javax.inject.Inject

class SkillViewModel @Inject constructor(
    private val stopwatchUtil: StopwatchUtil
) {
    val skill: LiveData<Skill> get() = _skill
    private val _skill = MutableLiveData<Skill>()
    val state = skill.map { it.toSkillState() }

    fun setSkill(value: Skill) {
        _skill.value = value
    }

    fun startTimer() {
        stopwatchUtil.start(this.skill.value!!.id)
    }

    val navigateToDetails = SingleLiveEvent<Any>()
    val startDrag = SingleLiveEvent<Any>()

    fun navigateToDetails() = navigateToDetails.call()

    fun startDrag(): Boolean {
        startDrag.call()
        return false
    }
}
