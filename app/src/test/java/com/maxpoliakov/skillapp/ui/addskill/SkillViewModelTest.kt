package com.maxpoliakov.skillapp.ui.addskill

import com.maxpoliakov.skillapp.setupThreads
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SkillViewModelTest : StringSpec({
    beforeSpec { setupThreads() }

    "inputIsValid" {
        val viewModel = SkillViewModel()
        viewModel.inputIsValid.observeForever {}
        viewModel.name.value = "New name"
        viewModel.inputIsValid.value shouldBe true
        viewModel.name.value = "       \t"
        viewModel.inputIsValid.value shouldBe false
    }
})
