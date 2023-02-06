package com.maxpoliakov.skillapp.ui.common.picker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PickerDialogViewModel : ViewModel() {
    private val _mode = MutableStateFlow(InputMode.Slider)
    val mode get() = _mode.asStateFlow()

    fun toggleInputMode() {
        _mode.value = if (_mode.value == InputMode.Keyboard) InputMode.Slider else InputMode.Keyboard
    }
}