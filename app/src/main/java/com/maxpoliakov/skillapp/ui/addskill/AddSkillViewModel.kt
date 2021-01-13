package com.maxpoliakov.skillapp.ui.addskill

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.skill.AddSkillUseCase
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import java.time.Duration

class AddSkillViewModel @ViewModelInject constructor(
    private val addSkill: AddSkillUseCase
) : SkillViewModel() {

    val totalTime = MutableLiveData<String>()
    val goToSkillDetail: LiveData<Int> get() = _goToSkillDetail
    private val _goToSkillDetail = SingleLiveEvent<Int>()

    fun addSkill() {
        viewModelScope.launch {
            val name = name.value.orEmpty().trim()
            val time = Duration.ofHours(totalTime.value?.toLongOrNull() ?: 0)

            val skillId = addSkill.run(
                Skill(name = name, totalTime = time, initialTime = time)
            )

            _goToSkillDetail.value = skillId.toInt()
        }
    }
}
