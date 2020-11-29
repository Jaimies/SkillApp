package com.maxpoliakov.skillapp.ui.skills

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.model.mapToPresentation
import com.maxpoliakov.skillapp.shared.util.mapList
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.lifecycle.launchCoroutine
import java.time.Duration

class SkillsViewModel @ViewModelInject constructor(
    getSkills: GetSkillsUseCase,
    private val addRecord: AddRecordUseCase
) : ViewModel() {

    val skills = getSkills.run()
        .mapList { it.mapToPresentation() }
        .asLiveData()

    val isEmpty = skills.map { it.isEmpty() }

    val navigateToAddEdit = SingleLiveEvent<Any>()

    fun createRecord(skill: SkillItem, time: Duration) = launchCoroutine {
        val record = Record(skill.name, skill.id, time)
        addRecord.run(record)
    }

    fun navigateToAddSkill() = navigateToAddEdit.call()
}
