package com.maxpoliakov.skillapp.ui.addskill

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.model.SkillItem

open class SkillViewModel(skill: SkillItem? = null) : ViewModel() {
    val name = MutableLiveData<String>(skill?.name)
    val inputIsValid = name.map { it?.isBlank() == false }
}
