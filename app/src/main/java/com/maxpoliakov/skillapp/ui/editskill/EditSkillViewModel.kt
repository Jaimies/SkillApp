package com.maxpoliakov.skillapp.ui.editskill

import com.maxpoliakov.skillapp.domain.usecase.skill.UpdateSkillUseCase
import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.ui.common.EditableViewModel
import com.maxpoliakov.skillapp.util.navigation.NavigationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditSkillViewModel(
    private val updateSkill: UpdateSkillUseCase,
    private val navigationUtil: NavigationUtil,
    private val skill: SkillItem,
    private val ioScope: CoroutineScope
) : EditableViewModel(skill.name) {

    override fun save(name: String) {
        ioScope.launch {
            updateSkill.updateName(skill.id, name)
        }

        navigationUtil.navigateUp()
    }

    class Factory @Inject constructor(
        private val updateSkill: UpdateSkillUseCase,
        private val navigationUtil: NavigationUtil,
        private val ioScope: CoroutineScope
    ) {
        fun create(skill: SkillItem) = EditSkillViewModel(
            updateSkill, navigationUtil, skill, ioScope
        )
    }
}
