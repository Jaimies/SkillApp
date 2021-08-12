package com.maxpoliakov.skillapp.ui.editgroup

import com.maxpoliakov.skillapp.domain.usecase.grouping.UpdateGroupUseCase
import com.maxpoliakov.skillapp.model.SkillGroupMinimal
import com.maxpoliakov.skillapp.ui.common.EditableViewModel
import com.maxpoliakov.skillapp.util.navigation.NavigationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditSkillGroupViewModel(
    private val group: SkillGroupMinimal,
    private val navigationUtil: NavigationUtil,
    private val updateGroup: UpdateGroupUseCase,
    private val ioScope: CoroutineScope,
) : EditableViewModel(group.name) {

    override fun save(name: String) {
        ioScope.launch {
            updateGroup.updateName(group.id, name)
        }

        navigateBack()
    }

    private fun navigateBack() = navigationUtil.navigateUp()

    class Factory @Inject constructor(
        private val updateGroup: UpdateGroupUseCase,
        private val navigationUtil: NavigationUtil,
        private val ioScope: CoroutineScope,
    ) {
        fun create(group: SkillGroupMinimal) = EditSkillGroupViewModel(
            group, navigationUtil, updateGroup, ioScope
        )
    }
}
