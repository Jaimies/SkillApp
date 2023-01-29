package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

inline fun <reified T> Context.getBaseContext() =
    if (this is T) this else (this as ContextWrapper).baseContext as T

fun RecyclerView.setupAdapter(adapter: RecyclerView.Adapter<*>) {
    if (layoutManager != null && this.adapter != null)
        return

    layoutManager = LinearLayoutManager(context)
    this.adapter = adapter
}

fun View.increaseTouchAreaBy(size: Unit) {
    val numberOfPx = size.toPx(context)
    val parent = parent as View
    parent.post {
        val rect = Rect()
        getHitRect(rect)
        rect.top -= numberOfPx
        rect.bottom += numberOfPx
        rect.left -= numberOfPx
        rect.right += numberOfPx
        parent.touchDelegate = TouchDelegate(rect, this)
    }
}
