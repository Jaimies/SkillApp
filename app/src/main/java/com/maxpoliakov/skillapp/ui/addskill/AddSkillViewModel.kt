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
        val name = name.value.orEmpty()
        if (name.isEmpty()) {
            nameIsEmpty.value = true
            return
        }

        ioScope.launch {
            addSkill.run(
                Skill(
                    name = name,
                    totalTime = Duration.ofHours(totalTime.value?.toLong() ?: 0)
                )
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
