package com.theskillapp.skillapp.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.theskillapp.skillapp.domain.model.Goal
import com.theskillapp.skillapp.domain.model.Timer
import com.theskillapp.skillapp.domain.model.Trackable
import com.theskillapp.skillapp.domain.stopwatch.Stopwatch
import com.theskillapp.skillapp.domain.usecase.stats.GetStatsUseCase
import com.theskillapp.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.theskillapp.skillapp.model.mapToUI
import com.theskillapp.skillapp.shared.chart.ChartDataImpl
import com.theskillapp.skillapp.shared.history.ViewModelWithHistory
import com.theskillapp.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import com.theskillapp.skillapp.shared.util.sumByDuration
import com.theskillapp.skillapp.shared.util.until
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

abstract class DetailsViewModel(
    stopwatch: Stopwatch,
    flow: Flow<Trackable>,
) : ViewModelWithHistory() {
    @Inject
    lateinit var chartDataFactory: ChartDataImpl.Factory

    @Inject
    lateinit var getStatsUseCase: GetStatsUseCase

    override val unitForDailyTotals get() = unitFlow

    val unit by lazy { unitFlow.map { it.mapToUI() }.asLiveData() }

    private val _mode = MutableStateFlow(Mode.View)
    val mode get() = _mode.asStateFlow()

    private val _onSave = SingleLiveEventWithoutData()
    val onSave: LiveData<Unit> get() = _onSave

    val name = MutableLiveData("")
    val inputIsValid = name.map { it?.isBlank() == false }

    private val _goal = MutableStateFlow<Goal?>(null)
    val goal by lazy { _goal.mapToUI(unitFlow).asLiveData() }

    private val _chooseGoal = SingleLiveEventWithoutData()
    val chooseGoal: LiveData<Unit> get() = _chooseGoal

    private val _navigateUp = SingleLiveEventWithoutData()
    val navigateUp: LiveData<Unit> get() = _navigateUp

    val lastWeekTime by lazy {
        getRecentCount.getLast7DayCount(selectionCriteria)
    }

    private val goalFlow = flow.map { it.goal }
    private val unitFlow = flow.map { it.unit }

    val chartData by lazy { chartDataFactory.create(flowOf(selectionCriteria), unitFlow, goalFlow) }

    private val recordedCountThisInterval = flow.flatMapLatest { trackable ->
        val goal = trackable.goal ?: return@flatMapLatest flowOf(0L)
        if (goal.type == Goal.Type.Lifetime) flowOf(trackable.totalCount)
        else getRecentCount.getCountSinceStartOfInterval(selectionCriteria, goal.type.interval)
    }

    private val tick = flow {
        while (true) {
            emit(Unit)
            delay(GOAL_PROGRESS_REFRESH_INTERVAL)
        }
    }

    private val timeOnStopwatch = stopwatch.state.combine(tick) { state, _ ->
        getApplicableTimers(state)
            .sumByDuration { it.startTime.until(ZonedDateTime.now()) }
    }

    private val _countThisInterval = recordedCountThisInterval.combine(timeOnStopwatch) { recordedCount, timeOnStopwatch ->
        recordedCount + timeOnStopwatch.toMillis()
    }

    val countThisInterval = _countThisInterval.asLiveData()

    val goalPercentage = combine(goalFlow, _countThisInterval) { goal, timeToday ->
        val goalCount = goal?.count ?: return@combine 0
        (timeToday * 100_000 / goalCount).toInt()
    }.asLiveData()

    init {
        viewModelScope.launch {
            val item = flow.first()

            name.value = item.name
            _goal.value = item.goal
        }
    }

    protected abstract fun getApplicableTimers(state: Stopwatch.State): List<Timer>

    private fun switchToEditMode() {
        _mode.value = Mode.Edit
    }

    fun onEditClicked() {
        if (_mode.value == Mode.Edit) save()
        else switchToEditMode()
    }

    fun onBackPressed() {
        if (mode.value == Mode.Edit) save()
        else _navigateUp.call()
    }

    abstract suspend fun update(name: String)

    fun save() {
        name.value?.trim()?.takeIf(String::isNotEmpty)?.let { name ->
            viewModelScope.launch { update(name) }
        }

        _mode.value = Mode.View
        _onSave.call()
    }


    fun setGoal(goal: Goal?) {
        _goal.value = goal
    }

    fun chooseGoal() = _chooseGoal.call()

    // todo consider extracting
    enum class Mode {
        View,
        Edit,
    }

    companion object {
        private const val GOAL_PROGRESS_REFRESH_INTERVAL = 2_000L
    }
}
