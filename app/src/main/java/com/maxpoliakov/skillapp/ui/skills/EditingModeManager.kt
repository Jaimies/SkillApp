package com.maxpoliakov.skillapp.ui.skills

import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditingModeManager @Inject constructor() {
    private val _isInEditingMode = MutableStateFlow(false)
    val isInEditingMode = _isInEditingMode.asLiveData()

    fun toggleEditingMode() {
        _isInEditingMode.value = !_isInEditingMode.value
    }
}
