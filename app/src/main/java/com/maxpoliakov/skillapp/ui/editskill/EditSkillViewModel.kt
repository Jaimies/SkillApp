package com.maxpoliakov.skillapp.ui.editskill

import androidx.lifecycle.LiveData
import com.maxpoliakov.skillapp.domain.usecase.skill.UpdateSkillUseCase
import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.ui.common.EditableViewModel
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditSkillViewModel(
    private val updateSkill: UpdateSkillUseCase,
    private val skill: SkillItem,
    private val ioScope: CoroutineScope
) : EditableViewModel(skill.name) {

    val navigateBack: LiveData<Any> get() = _navigateBack
    private val _navigateBack = SingleLiveEvent<Any>()

    override fun save(name: String) {
        ioScope.launch {
            updateSkill.updateName(skill.id, name)
        }

        navigateBack()
    }

    private fun navigateBack() = _navigateBack.call()

    class Factory @Inject constructor(
        private val updateSkill: UpdateSkillUseCase,
        private val ioScope: CoroutineScope
    ) {
        fun create(skill: SkillItem) = EditSkillViewModel(
            updateSkill, skill, ioScope
        )
    }
}
