package com.maxpoliakov.skillapp.ui.editskill

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.usecase.skill.DeleteSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.SaveSkillUseCase
import com.maxpoliakov.skillapp.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.ui.addskill.SkillViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditSkillViewModel(
    private val saveSkill: SaveSkillUseCase,
    private val deleteSkill: DeleteSkillUseCase,
    private val skill: SkillItem,
    private val ioScope: CoroutineScope
) : SkillViewModel(skill) {

    val showDeleteDialog = SingleLiveEvent<Any>()

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

    fun deleteSkill() {
        ioScope.launch {
            deleteSkill.run(skill.mapToDomain())
        }
    }

    fun showDeleteDialog() = showDeleteDialog.call()

    class Factory @Inject constructor(
        private val saveSkill: SaveSkillUseCase,
        private val deleteSkill: DeleteSkillUseCase,
        private val ioScope: CoroutineScope
    ) {
        fun create(skill: SkillItem) = EditSkillViewModel(
            saveSkill, deleteSkill, skill, ioScope
        )
    }
}
