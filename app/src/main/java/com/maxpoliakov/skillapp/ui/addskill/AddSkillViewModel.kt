package com.maxpoliakov.skillapp.ui.addskill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.skill.AddSkillUseCase
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.ui.common.EditableViewModel
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSkillViewModel @Inject constructor(
    private val addSkill: AddSkillUseCase,
) : EditableViewModel() {
    val totalTime = MutableLiveData<String>()

    private val _chooseGoal = SingleLiveEvent<Any>()
    val chooseGoal: LiveData<Any> get() = _chooseGoal

    val goToSkillDetail: LiveData<Int> get() = _goToSkillDetail
    private val _goToSkillDetail = SingleLiveEvent<Int>()

    private val _goal = MutableStateFlow<Goal?>(null)
    val goal = _goal.asLiveData()

    private val _unit = MutableLiveData(UiMeasurementUnit.Millis)
    val unit : LiveData<UiMeasurementUnit> get() = _unit

    fun setMeasurementUnitIndex(index: Int) {
        _unit.value = UiMeasurementUnit.values()[index]
    }

    override fun save(name: String) {
        viewModelScope.launch {
            val count = unit.value!!.getInitialCount(totalTime.value?.toLongOrNull() ?: 0L)

            val skillId = addSkill.run(
                Skill(
                    name = name,
                    totalCount = count,
                    initialCount = count,
                    goal = goal.value,
                    unit = unit.value!!.toDomain(),
                )
            )

            _goToSkillDetail.value = skillId.toInt()
        }
    }

    fun setGoal(goal: Goal?) {
        _goal.value = goal
    }

    fun chooseGoal() = _chooseGoal.call()
}
