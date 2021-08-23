package com.maxpoliakov.skillapp.ui.addskill

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.skill.AddSkillUseCase
import com.maxpoliakov.skillapp.setupThreads
import com.maxpoliakov.skillapp.test.any
import io.kotest.core.spec.style.StringSpec
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.Duration
import java.time.Duration.ZERO

class AddSkillViewModelTest : StringSpec({
    lateinit var viewModel: AddSkillViewModel
    lateinit var useCase: AddSkillUseCase

    beforeSpec { setupThreads() }

    beforeEach {
        useCase = mock(AddSkillUseCase::class.java)
        `when`(useCase.run(any())).thenReturn(1)
        viewModel = AddSkillViewModel(useCase)
    }

    "addSkill()" {
        viewModel.totalTime.value = "100"
        viewModel.name.value = "Skill name "
        viewModel.update()
        verify(useCase).run(Skill("Skill name", Duration.ofHours(100), Duration.ofHours(100)))
    }

    "addSkill() uses ZERO as the time if the totalTime field is null" {
        viewModel.totalTime.value = null
        viewModel.name.value = "Name"
        viewModel.update()
        verify(useCase).run(Skill("Name", ZERO, ZERO))
    }

    "addSkill() uses ZERO as the time if the totalTime field is empty" {
        viewModel.totalTime.value = ""
        viewModel.name.value = "Name"
        viewModel.update()
        verify(useCase).run(Skill("Name", ZERO, ZERO))
    }
})
