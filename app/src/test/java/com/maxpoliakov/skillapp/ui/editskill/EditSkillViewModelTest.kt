package com.maxpoliakov.skillapp.ui.editskill

import com.maxpoliakov.skillapp.domain.usecase.skill.SaveSkillUseCase
import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.setupThreads
import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Unconfined
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.Duration.ZERO
import java.time.LocalDate

class EditSkillViewModelTest : StringSpec({
    lateinit var useCase: SaveSkillUseCase
    lateinit var viewModel: EditSkillViewModel

    beforeSpec { setupThreads() }

    beforeEach {
        useCase = mock(SaveSkillUseCase::class.java)
        viewModel = EditSkillViewModel(useCase, createSkillItem(), CoroutineScope(Unconfined))
    }

    "updateSkill()" {
        viewModel.name.value = "New Name    "
        viewModel.updateSkill()
        verify(useCase).run(createSkillItem("New Name").mapToDomain())
    }
})

private fun createSkillItem(name: String = "name"): SkillItem {
    return SkillItem(0, name, ZERO, ZERO, ZERO, LocalDate.ofEpochDay(0), 0)
}
