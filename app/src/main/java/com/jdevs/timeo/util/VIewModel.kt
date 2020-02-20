package com.jdevs.timeo.util

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.reflect.KClass

inline fun ViewModel.launchCoroutine(crossinline block: suspend () -> Unit) {

    viewModelScope.launch { block() }
}

fun <T : ViewModel> createViewModel(fragmentActivity: FragmentActivity, modelClass: KClass<T>): T {

    return ViewModelProvider(fragmentActivity).get(UUID.randomUUID().toString(), modelClass.java)
}
