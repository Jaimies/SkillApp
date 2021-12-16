package com.maxpoliakov.skillapp.ui.skills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SkillViewModel @Inject constructor(
    private val stopwatchUtil: StopwatchUtil,
    private val ioScope: CoroutineScope,
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

    private val _notifyRecordAdded = SingleLiveEvent<Record>()
    val notifyRecordAdded: LiveData<Record> get() = _notifyRecordAdded

    fun setSkill(value: Skill) {
        _skill.value = value
        _isSmall.value = value.groupId != -1
    }

    fun startTimer() {
        ioScope.launch {
            val record = stopwatchUtil.start(skill.value!!.id)
            if (record != null)
                withContext(Dispatchers.Main) { _notifyRecordAdded.value = record }
        }
        logEvent("start_timer_from_home_screen")
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
