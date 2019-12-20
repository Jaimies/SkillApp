package com.jdevs.timeo.common.viewmodel

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.util.SingleLiveEvent

open class KeyboardHidingViewModel : ViewModel() {

    val hideKeyboard = SingleLiveEvent<Any>()

    fun hideKeyboard() {

        hideKeyboard.call()
    }
}
