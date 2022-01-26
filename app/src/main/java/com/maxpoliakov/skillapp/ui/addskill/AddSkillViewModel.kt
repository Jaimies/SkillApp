package com.maxpoliakov.skillapp.ui.addskill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.TimeTarget
import com.maxpoliakov.skillapp.domain.usecase.skill.AddSkillUseCase
import com.maxpoliakov.skillapp.ui.common.EditableViewModel
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class AddSkillViewModel @Inject constructor(
    private val addSkill: AddSkillUseCase
) : EditableViewModel() {

    val totalTime = MutableLiveData<String>()

    private val _chooseGoal = SingleLiveEvent<Any>()
    val chooseGoal: LiveData<Any> get() = _chooseGoal

    val goToSkillDetail: LiveData<Int> get() = _goToSkillDetail
    private val _goToSkillDetail = SingleLiveEvent<Int>()

    private val _goal = MutableStateFlow<Duration?>(null)
    val goal = _goal.asLiveData()

    override fun save(name: String) {
        viewModelScope.launch {
            val time = Duration.ofHours(totalTime.value?.toLongOrNull() ?: 0)

            val goal = _goal.value?.let { goal -> TimeTarget(goal, TimeTarget.Interval.Daily) }

            val skillId = addSkill.run(
                Skill(name = name, totalTime = time, initialTime = time, target = goal)
            )

            _goToSkillDetail.value = skillId.toInt()
        }
    }

    fun setGoal(duration: Duration) {
        _goal.value = duration
    }

    fun chooseGoal() = _chooseGoal.call()
}
