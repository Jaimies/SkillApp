package com.jdevs.timeo.util.lifecycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

inline fun ViewModel.launchCoroutine(crossinline block: suspend () -> Unit) {

    viewModelScope.launch { block() }
}
