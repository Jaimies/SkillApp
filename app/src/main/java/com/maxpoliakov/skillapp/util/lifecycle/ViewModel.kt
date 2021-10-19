package com.maxpoliakov.skillapp.util.lifecycle

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModels(
    noinline viewModelProducer: () -> VM
) = viewModels<VM> {
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(VM::class.java)) {
                "ViewModel not found: $modelClass"
            }

            @Suppress("UNCHECKED_CAST")
            return viewModelProducer() as T
        }
    }
}
