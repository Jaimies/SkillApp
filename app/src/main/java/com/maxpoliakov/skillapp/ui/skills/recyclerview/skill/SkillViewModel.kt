package com.maxpoliakov.skillapp.ui.skills.recyclerview.skill

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.di.ApplicationScope
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch.StateChange
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.shared.analytics.logEvent
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import com.maxpoliakov.skillapp.shared.permissions.PermissionRequester
import com.maxpoliakov.skillapp.ui.skills.EditingModeManager
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
    private val permissionRequester: PermissionRequester,
    @ApplicationScope
    private val scope: CoroutineScope,
) {
    private val _skill = MutableStateFlow<Skill?>(null)
    val skill = _skill.asStateFlow()

    val unit = _skill.map { skill -> skill?.unit?.mapToUI() }.asLiveData()

    private val _navigateToDetails = SingleLiveEvent<Skill>()
    val navigateToDetails: LiveData<Skill> get() = _navigateToDetails

    private val _showRecordsAdded = SingleLiveEvent<List<Record>>()
    val showRecordsAdded: LiveData<List<Record>> get() = _showRecordsAdded

    val dragHandleShown = manager.isInEditingMode
    val groupId get() = skill.value?.groupId ?: -1

    val isStopwatchActive = stopwatch.state.combine(_skill) { state, skill ->
        skill?.let { state.hasTimerForSkillId(skill.id)}
    }.asLiveData()

    fun setSkill(skill: Skill) {
        _skill.value = skill
    }

    fun toggleTimer() {
        scope.launch {
            val stateChange = stopwatch.toggle(skill.value!!.id)
            _showRecordsAdded.value = stateChange.addedRecords

            if (stateChange is StateChange.Start) {
                permissionRequester.requestNotificationPermissionIfNotGranted()
            }
        }
        logEvent("start_timer_from_home_screen")
    }

    val startDrag = SingleLiveEventWithoutData()

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
