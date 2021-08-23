package com.maxpoliakov.skillapp.ui.addskill

import com.maxpoliakov.skillapp.setupThreads
import com.maxpoliakov.skillapp.ui.common.EditableViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EditableViewModelImpl : EditableViewModel() {
    override fun save(name: String) {}
}

class SkillViewModelTest : StringSpec({
    beforeSpec { setupThreads() }

    "inputIsValid" {
        val viewModel = EditableViewModelImpl()
        viewModel.inputIsValid.observeForever {}
        viewModel.name.value = "New name"
        viewModel.inputIsValid.value shouldBe true
        viewModel.name.value = "       \t"
        viewModel.inputIsValid.value shouldBe false
    }
})
