package com.maxpoliakov.skillapp.ui.skills.recyclerview.skill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.di.coroutines.ApplicationScope
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.ui.skills.EditingModeManager
import com.maxpoliakov.skillapp.shared.analytics.logEvent
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SkillViewModel @Inject constructor(
    private val stopwatch: Stopwatch,
    manager: EditingModeManager,
    @ApplicationScope
    private val scope: CoroutineScope,
) {
    private val _skill = MutableStateFlow<Skill?>(null)
    val skill = _skill.asStateFlow()

    val unit = _skill.map { skill -> skill?.unit?.mapToUI() }.asLiveData()

    private val _isSmall = MutableLiveData(false)
    val isSmall: LiveData<Boolean> get() = _isSmall

    private val _isHighlighted = MutableLiveData(false)
    val isHighlighted: LiveData<Boolean> get() = _isHighlighted

    private val _navigateToDetails = SingleLiveEvent<Skill>()
    val navigateToDetails: LiveData<Skill> get() = _navigateToDetails

    private val _notifyRecordAdded = SingleLiveEvent<Record>()
    val notifyRecordAdded: LiveData<Record> get() = _notifyRecordAdded

    val dragHandleShown = manager.isInEditingMode
    val groupId get() = skill.value?.groupId ?: -1

    val isStopwatchActive = stopwatch.state.combine(_skill) { state, skill ->
        state is StopwatchState.Running && state.skillId == skill?.id
    }.asLiveData()

    fun setSkill(value: Skill) {
        _skill.value = value
        _isSmall.value = value.groupId != -1
    }

    fun toggleTimer() {
        scope.launch {
            val record = stopwatch.toggle(skill.value!!.id)
            if (record != null) _notifyRecordAdded.value = record
        }
        logEvent("start_timer_from_home_screen")
    }

    fun setIsSmall(isSmall: Boolean) {
        _isSmall.value = isSmall
    }

    fun setIsHighlighted(isHighlighted: Boolean) {
        _isHighlighted.value = isHighlighted
    }

    val startDrag = SingleLiveEvent<Any>()

    fun navigateToDetails() {
        skill.value?.let { skill ->
            _navigateToDetails.value = skill
        }
    }

    fun startDrag(): Boolean {
        startDrag.call()
        return true
    }
}
