package com.maxpoliakov.skillapp.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.model.Trackable
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.domain.usecase.stats.GetRecentCountUseCase
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.model.mapToUI
import com.maxpoliakov.skillapp.shared.util.until
import com.maxpoliakov.skillapp.ui.common.history.ViewModelWithHistory
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.ZonedDateTime
import javax.inject.Inject

abstract class DetailsViewModel(
    stopwatchUtil: StopwatchUtil,
    getRecentTime: GetRecentCountUseCase,
    flow: Flow<Trackable>,
) : ViewModelWithHistory() {

    @Inject
    lateinit var chartDataFactory: ChartData.Factory

    val chartData by lazy { chartDataFactory.create(selectionCriteria) }

    abstract val unitFlow: Flow<MeasurementUnit>

    override val unitForDailyTotals get() = unitFlow

    private val uiUnitFlow by lazy { unitFlow.map { it.mapToUI() } }
    val unit by lazy { uiUnitFlow.asLiveData() }
    protected abstract val nameFlow: Flow<String>

    private val _isEditing = MutableLiveData(false)
    val isEditing: LiveData<Boolean> get() = _isEditing

    private val _onSave = SingleLiveEvent<Nothing>()
    val onSave: LiveData<Nothing> get() = _onSave

    val name = MutableLiveData("")
    val inputIsValid = name.map { it?.isBlank() == false }

    private val _goal = MutableStateFlow<Goal?>(null)
    val goal by lazy { _goal.mapToUI(uiUnitFlow).asLiveData() }

    private val _chooseGoal = SingleLiveEvent<Any>()
    val chooseGoal: LiveData<Any> get() = _chooseGoal

    private var lastName = ""

    private val goalFlow = flow.map { it.goal }

    private val recordedCountFlow = flow.flatMapLatest { trackable ->
        val goal = trackable.goal ?: return@flatMapLatest flowOf(0L)
        getRecentTime.getCountSinceStartOfInterval(trackable.id, goal.type.interval)
    }

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
            nameFlow.first().let { newName ->
                name.value = newName
                lastName = newName
            }

            _goal.value = goalFlow.first()
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
