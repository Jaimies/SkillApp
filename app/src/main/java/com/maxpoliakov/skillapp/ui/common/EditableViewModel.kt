package com.maxpoliakov.skillapp.ui.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

abstract class EditableViewModel(initialName: String = "") : ViewModel() {
    val name = MutableLiveData<String>(initialName)

    val inputIsValid = name.map { it?.isBlank() == false }

    fun update() {
        save(name.value.orEmpty().trim())
    }

    protected abstract fun save(name: String)
}
