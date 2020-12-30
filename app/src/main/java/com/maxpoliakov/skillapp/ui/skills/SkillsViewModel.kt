package com.maxpoliakov.skillapp.ui.skills

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsUseCase
import com.maxpoliakov.skillapp.model.mapToPresentation
import com.maxpoliakov.skillapp.shared.util.mapList
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent

class SkillsViewModel @ViewModelInject constructor(
    getSkills: GetSkillsUseCase
) : ViewModel() {

    val skills = getSkills.run()
        .mapList { it.mapToPresentation() }
        .asLiveData()

    val isEmpty = skills.map { it.isEmpty() }

    val navigateToAddEdit = SingleLiveEvent<Any>()
    fun navigateToAddSkill() = navigateToAddEdit.call()
}
