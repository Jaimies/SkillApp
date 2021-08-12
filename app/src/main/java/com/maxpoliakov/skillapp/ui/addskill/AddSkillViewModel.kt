package com.maxpoliakov.skillapp.ui.addskill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.skill.AddSkillUseCase
import com.maxpoliakov.skillapp.ui.common.EditableViewModel
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class AddSkillViewModel @Inject constructor(
    private val addSkill: AddSkillUseCase
) : EditableViewModel() {

    val totalTime = MutableLiveData<String>()
    val goToSkillDetail: LiveData<Int> get() = _goToSkillDetail
    private val _goToSkillDetail = SingleLiveEvent<Int>()

    override fun save(name: String) {
        viewModelScope.launch {
            val time = Duration.ofHours(totalTime.value?.toLongOrNull() ?: 0)

            val skillId = addSkill.run(
                Skill(name = name, totalTime = time, initialTime = time)
            )

            _goToSkillDetail.value = skillId.toInt()
        }
    }
}
