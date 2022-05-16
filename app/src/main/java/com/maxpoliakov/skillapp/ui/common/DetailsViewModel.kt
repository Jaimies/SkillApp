package com.maxpoliakov.skillapp.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.stats.GetTotalTimeAtDayUseCase
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.shared.util.collectOnce
import com.maxpoliakov.skillapp.shared.util.until
import com.maxpoliakov.skillapp.ui.common.history.ViewModelWithHistory
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.ZonedDateTime

abstract class DetailsViewModel(
    stopwatchUtil: StopwatchUtil,
    goalFlow: Flow<Goal?>,
    recordedCountFlow: Flow<Long>,
    getTotalTimeAtDay: GetTotalTimeAtDayUseCase,
) : ViewModelWithHistory(getTotalTimeAtDay) {
    abstract val unit: LiveData<UiMeasurementUnit>

    protected abstract val nameFlow: Flow<String>

    private val _isEditing = MutableLiveData(false)
    val isEditing: LiveData<Boolean> get() = _isEditing

    private val _onSave = SingleLiveEvent<Nothing>()
    val onSave: LiveData<Nothing> get() = _onSave

    val name = MutableLiveData("")
    val inputIsValid = name.map { it?.isBlank() == false }

    private val _goal = MutableStateFlow<Goal?>(null)
    val goal = _goal.asLiveData()

    private val _chooseGoal = SingleLiveEvent<Any>()
    val chooseGoal: LiveData<Any> get() = _chooseGoal

    private var lastName = ""

    private val tick = flow {
        while (true) {
            emit(Unit)
            delay(GOAL_PROGRESS_REFRESH_INTERVAL)
        }
    }

    private val timeOnStopwatch = stopwatchUtil.state.combine(tick) { state, _ ->
        if (state !is StopwatchState.Running || !isStopwatchTracking(state))
            return@combine Duration.ZERO

        state.startTime.until(ZonedDateTime.now())
    }

    private val _timeToday = recordedCountFlow.combine(timeOnStopwatch) { recordedCount, timeOnStopwatch ->
        recordedCount + timeOnStopwatch.toMillis()
    }

    val timeToday = _timeToday.asLiveData()

    val goalPercentage = combine(goalFlow, _timeToday) { goal, timeToday ->
        val goalCount = goal?.count ?: return@combine 0
        (timeToday * 100_000 / goalCount).toInt()
    }.asLiveData()

    init {
        viewModelScope.launch {
            delay(1)
            nameFlow.collectOnce { newName ->
                name.value = newName
                lastName = newName
            }

            goalFlow.collectOnce { goal -> _goal.value = goal }
        }
    }

    protected abstract fun isStopwatchTracking(state: StopwatchState.Running): Boolean

    fun enterEditingMode() {
        _isEditing.value = true
    }

    fun exitEditingMode() {
        name.value = lastName
        _isEditing.value = false
    }

    abstract suspend fun update(name: String)

    fun save() {
        name.value?.trim()?.takeIf(String::isNotEmpty)?.let { name ->
            viewModelScope.launch { update(name) }
            lastName = name
        }

        _isEditing.value = false
        _onSave.call()
    }


    fun setGoal(goal: Goal?) {
        _goal.value = goal
    }

    fun chooseGoal() = _chooseGoal.call()

    companion object {
        private const val GOAL_PROGRESS_REFRESH_INTERVAL = 2_000L
    }
}
