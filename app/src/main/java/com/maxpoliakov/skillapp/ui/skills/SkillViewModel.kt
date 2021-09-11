package com.maxpoliakov.skillapp.ui.skills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
import javax.inject.Inject

class SkillViewModel @Inject constructor(
    private val stopwatchUtil: StopwatchUtil,
) {
    val skill: LiveData<Skill> get() = _skill
    private val _skill = MutableLiveData<Skill>()
    val state = skill.map { it.toSkillState() }

    private val _isSmall = MutableLiveData(false)
    val isSmall: LiveData<Boolean> get() = _isSmall

    private val _isHighlighted = MutableLiveData(false)
    val isHighlighted: LiveData<Boolean> get() = _isHighlighted

    private val _navigateToDetails = SingleLiveEvent<Skill>()
    val navigateToDetails: LiveData<Skill> get() = _navigateToDetails

    fun setSkill(value: Skill) {
        _skill.value = value
        _isSmall.value = value.groupId != -1
    }

    fun startTimer() {
        stopwatchUtil.start(this.skill.value!!.id)
    }

    fun setIsSmall(isSmall: Boolean) = _isSmall.setValue(isSmall)
    fun setIsHighlighted(isHighlighted: Boolean) = _isHighlighted.setValue(isHighlighted)

    val startDrag = SingleLiveEvent<Any>()

    fun navigateToDetails() {
        skill.value?.let { skill ->
            _navigateToDetails.value = skill
        }
    }

    fun startDrag(): Boolean {
        startDrag.call()
        return false
    }
}
