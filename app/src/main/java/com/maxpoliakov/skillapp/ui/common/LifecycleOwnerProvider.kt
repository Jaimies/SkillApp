package com.maxpoliakov.skillapp.ui.common

import androidx.lifecycle.LifecycleOwner

fun interface LifecycleOwnerProvider {
    fun get(): LifecycleOwner
}
