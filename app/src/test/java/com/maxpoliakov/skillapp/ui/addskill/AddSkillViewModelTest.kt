package com.maxpoliakov.skillapp.ui.addskill

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.skill.ManageSkillUseCase
import com.maxpoliakov.skillapp.resetThreads
import com.maxpoliakov.skillapp.setupThreads
import io.kotest.core.spec.style.StringSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.Duration

class AddSkillViewModelTest : StringSpec({
    lateinit var viewModel: AddSkillViewModel
    lateinit var useCase: ManageSkillUseCase

    beforeSpec { setupThreads() }
    afterSpec { resetThreads() }

    beforeEach {
        useCase = mockk<ManageSkillUseCase>()
        coEvery { useCase.addSkill(any()) } returns 1
        viewModel = AddSkillViewModel(useCase)
    }

    "addSkill()" {
        viewModel.totalTime.value = "100"
        viewModel.name.value = "Skill name "
        viewModel.update()

        coVerify {
            useCase.addSkill(
                Skill(
                    "Skill name",
                    MeasurementUnit.Millis,
                    Duration.ofHours(100).toMillis(),
                    Duration.ofHours(100).toMillis(),
                )
            )
        }
    }

    "addSkill() uses ZERO as the time if the totalTime field is null" {
        viewModel.totalTime.value = null
        viewModel.name.value = "Name"
        viewModel.update()

        coVerify {
            useCase.addSkill(Skill("Name", MeasurementUnit.Millis, 0, 0))
        }
    }

    "addSkill() uses ZERO as the time if the totalTime field is empty" {
        viewModel.totalTime.value = ""
        viewModel.name.value = "Name"
        viewModel.update()
        coVerify {
            useCase.addSkill(Skill("Name", MeasurementUnit.Millis, 0, 0))
        }
    }
})
