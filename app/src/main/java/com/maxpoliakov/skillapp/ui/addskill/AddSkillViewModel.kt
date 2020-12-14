package com.maxpoliakov.skillapp.ui.addskill

import androidx.lifecycle.MutableLiveData
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.skill.AddSkillUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

class AddSkillViewModel(
    private val addSkill: AddSkillUseCase,
    private val ioScope: CoroutineScope
) : SkillViewModel() {

    val totalTime = MutableLiveData<String>()

    fun addSkill() {
        ioScope.launch {
            val name = name.value.orEmpty().trim()
            val time = Duration.ofHours(totalTime.value?.toLongOrNull() ?: 0)

            addSkill.run(
                Skill(name = name, totalTime = time, initialTime = time)
            )
        }

        navigateBack()
    }

    class Factory @Inject constructor(
        private val addSkill: AddSkillUseCase,
        private val ioScope: CoroutineScope
    ) {
        fun create() = AddSkillViewModel(addSkill, ioScope)
    }
}
