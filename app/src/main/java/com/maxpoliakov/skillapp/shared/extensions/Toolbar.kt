package com.maxpoliakov.skillapp.shared.extensions

import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import kotlinx.coroutines.delay

suspend fun Toolbar.getViewByIdWhenReady(@IdRes itemId: Int): View {
    while (true) {
        delay(1)
        findViewById<View>(itemId)?.let { return it }
    }
}
