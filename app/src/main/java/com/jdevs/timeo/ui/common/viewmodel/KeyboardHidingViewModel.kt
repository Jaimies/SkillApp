package com.jdevs.timeo.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.util.SingleLiveEvent

abstract class KeyboardHidingViewModel : ViewModel() {

    val hideKeyboard = SingleLiveEvent<Any>()
    fun hideKeyboard() = hideKeyboard.call()
}