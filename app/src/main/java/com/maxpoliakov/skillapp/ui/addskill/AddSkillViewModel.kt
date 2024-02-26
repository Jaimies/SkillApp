package com.maxpoliakov.skillapp.ui.addskill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.skill.ManageSkillUseCase
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.model.mapToUI
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSkillViewModel @Inject constructor(
    private val manageSkill: ManageSkillUseCase,
) : ViewModel() {
    val name = MutableLiveData("")
    val totalTime = MutableLiveData<String>()

    val inputIsValid = name.map { it?.isBlank() == false }

    private val _chooseGoal = SingleLiveEventWithoutData()
    val chooseGoal: LiveData<Unit> get() = _chooseGoal

    private val _navigateUp = SingleLiveEventWithoutData()
    val navigateUp: LiveData<Unit> get() = _navigateUp

    private val _unit = MutableStateFlow(UiMeasurementUnit.Millis)
    val unit = _unit.asLiveData()

    private val _goal = MutableStateFlow<Goal?>(null)
    val goal = _goal.mapToUI(_unit).asLiveData()

    fun setMeasurementUnitIndex(index: Int) {
        _unit.value = UiMeasurementUnit.values()[index]
    }

    fun update() {
        viewModelScope.launch {
            val name = name.value.orEmpty().trim()
            val count = _unit.value.getInitialCount(totalTime.value?.toLongOrNull() ?: 0L)

            manageSkill.addSkill(
                Skill(
                    name = name,
                    totalCount = count,
                    initialCount = count,
                    goal = _goal.value,
                    unit = _unit.value.domainCounterpart,
                )
            )

            _navigateUp.call()
        }
    }

    fun setGoal(goal: Goal?) {
        _goal.value = goal
    }

    fun chooseGoal() = _chooseGoal.call()
}
