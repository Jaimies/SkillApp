package com.maxpoliakov.skillapp.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent

abstract class KeyboardHidingViewModel : ViewModel() {
    val hideKeyboard = SingleLiveEvent<Any>()
    fun hideKeyboard() = hideKeyboard.call()
}
