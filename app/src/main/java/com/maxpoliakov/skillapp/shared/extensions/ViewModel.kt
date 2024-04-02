package com.maxpoliakov.skillapp.shared.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.withCreationCallback

inline fun <Factory, reified VM : ViewModel> Fragment.viewModelFromAssistedFactory(
    noinline producer: (Factory) -> VM
) = viewModels<VM>(
    extrasProducer = {
        defaultViewModelCreationExtras.withCreationCallback(producer)
    }
)
