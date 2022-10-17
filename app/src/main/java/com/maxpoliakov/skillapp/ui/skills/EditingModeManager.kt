package com.maxpoliakov.skillapp.ui.skills

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditingModeManager @Inject constructor() {
    private val _isInEditingMode = MutableStateFlow(false)
    val isInEditingMode = _isInEditingMode.asStateFlow()

    fun toggleEditingMode() {
        _isInEditingMode.value = !_isInEditingMode.value
    }
}
