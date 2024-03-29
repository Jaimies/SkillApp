package com.maxpoliakov.skillapp.ui.addskill

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.skill.ManageSkillUseCase
import com.maxpoliakov.skillapp.setupThreads
import com.maxpoliakov.skillapp.test.any
import io.kotest.core.spec.style.StringSpec
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.Duration

class AddSkillViewModelTest : StringSpec({
    lateinit var viewModel: AddSkillViewModel
    lateinit var useCase: ManageSkillUseCase

    beforeSpec { setupThreads() }

    beforeEach {
        useCase = mock(ManageSkillUseCase::class.java)
        `when`(useCase.addSkill(any())).thenReturn(1)
        viewModel = AddSkillViewModel(useCase)
    }

    "addSkill()" {
        viewModel.totalTime.value = "100"
        viewModel.name.value = "Skill name "
        viewModel.update()
        verify(useCase).addSkill(Skill("Skill name", MeasurementUnit.Millis, Duration.ofHours(100).toMillis(), Duration.ofHours(100).toMillis()))
    }

    "addSkill() uses ZERO as the time if the totalTime field is null" {
        viewModel.totalTime.value = null
        viewModel.name.value = "Name"
        viewModel.update()
        verify(useCase).addSkill(Skill("Name", MeasurementUnit.Millis, 0, 0))
    }

    "addSkill() uses ZERO as the time if the totalTime field is empty" {
        viewModel.totalTime.value = ""
        viewModel.name.value = "Name"
        viewModel.update()
        verify(useCase).addSkill(Skill("Name", MeasurementUnit.Millis, 0, 0))
    }
})
