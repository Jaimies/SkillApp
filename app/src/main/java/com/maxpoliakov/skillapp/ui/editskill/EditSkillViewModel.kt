package com.maxpoliakov.skillapp.ui.editskill

import com.maxpoliakov.skillapp.domain.usecase.skill.SaveSkillUseCase
import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.ui.addskill.SkillViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditSkillViewModel(
    private val saveSkill: SaveSkillUseCase,
    private val skill: SkillItem,
    private val ioScope: CoroutineScope
) : SkillViewModel(skill) {

    fun updateSkill() {
        val name = name.value.orEmpty()
        if (name.isEmpty()) {
            nameIsEmpty.value = true
            return
        }

        ioScope.launch {
            saveSkill.run(skill.mapToDomain().copy(name = name))
        }

        navigateBack()
    }

    class Factory @Inject constructor(
        private val saveSkill: SaveSkillUseCase,
        private val ioScope: CoroutineScope
    ) {
        fun create(skill: SkillItem) = EditSkillViewModel(
            saveSkill, skill, ioScope
        )
    }
}
