package com.maxpoliakov.skillapp.ui.editskill

import com.maxpoliakov.skillapp.domain.usecase.skill.UpdateSkillUseCase
import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.setupThreads
import com.maxpoliakov.skillapp.util.navigation.NavigationUtil
import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Unconfined
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.Duration.ZERO
import java.time.LocalDate

class EditSkillViewModelTest : StringSpec({
    lateinit var useCase: UpdateSkillUseCase
    lateinit var navigationUtil: NavigationUtil
    lateinit var viewModel: EditSkillViewModel

    beforeSpec { setupThreads() }

    beforeEach {
        useCase = mock(UpdateSkillUseCase::class.java)
        navigationUtil = mock(NavigationUtil::class.java)

        viewModel = EditSkillViewModel(useCase, navigationUtil, createSkillItem(), CoroutineScope(Unconfined))
    }

    "updateSkill()" {
        viewModel.name.value = "New Name    "
        viewModel.update()
        verify(useCase).updateName(0, "New name")
    }
})

private fun createSkillItem(name: String = "name"): SkillItem {
    return SkillItem(0, name, ZERO, ZERO, ZERO, LocalDate.ofEpochDay(0), 0)
}
