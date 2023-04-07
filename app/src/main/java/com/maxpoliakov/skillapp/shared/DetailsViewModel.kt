package com.maxpoliakov.skillapp.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.Trackable
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import com.maxpoliakov.skillapp.domain.usecase.stats.GetRecentCountUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.model.mapToUI
import com.maxpoliakov.skillapp.shared.util.until
import com.maxpoliakov.skillapp.shared.history.ViewModelWithHistory
import com.maxpoliakov.skillapp.shared.chart.ChartDataImpl
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEventWithoutData
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
import java.time.Duration
import java.time.ZonedDateTime
import javax.inject.Inject

abstract class DetailsViewModel(
    stopwatch: Stopwatch,
    getRecentTime: GetRecentCountUseCase,
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
        getStatsUseCase.getLast7DayCount(selectionCriteria)
    }

    private var lastName = ""

    private val goalFlow = flow.map { it.goal }
    private val unitFlow = flow.map { it.unit }
    private val nameFlow = flow.map { it.name }

    val chartData by lazy { chartDataFactory.create(flowOf(selectionCriteria), flowOf(null), unitFlow, goalFlow) }

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

    private val timeOnStopwatch = stopwatch.state.combine(tick) { state, _ ->
        if (state !is Stopwatch.State.Running || !isStopwatchTracking(state))
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

    protected abstract fun isStopwatchTracking(state: Stopwatch.State.Running): Boolean

    fun switchToEditMode() {
        _mode.value = Mode.Edit
    }

    fun switchToViewMode() {
        name.value = lastName
        _mode.value = Mode.View
    }

    fun onEditClicked() {
        if (_mode.value == Mode.Edit) save()
        else switchToEditMode()
    }

    fun onBackPressed() {
        if (mode.value == Mode.Edit) {
            switchToViewMode()
        } else {
            _navigateUp.call()
        }
    }

    abstract suspend fun update(name: String)

    fun save() {
        name.value?.trim()?.takeIf(String::isNotEmpty)?.let { name ->
            viewModelScope.launch { update(name) }
            lastName = name
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
