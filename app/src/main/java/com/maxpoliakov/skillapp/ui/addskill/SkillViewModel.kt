package com.maxpoliakov.skillapp.ui.addskill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent

open class SkillViewModel(skill: SkillItem? = null) : ViewModel() {
    val name = MutableLiveData<String>(skill?.name)
    val inputIsValid = name.map { it?.isBlank() == false }
    val navigateBack: LiveData<Any> get() = _navigateBack
    private val _navigateBack = SingleLiveEvent<Any>()

    protected fun navigateBack() = _navigateBack.call()
}
