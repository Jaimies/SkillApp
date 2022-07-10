package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

val ViewGroup.fragmentActivity get() = context.getBaseContext() as FragmentActivity

inline fun <reified T> Context.getBaseContext() =
    if (this is T) this else (this as ContextWrapper).baseContext as T


fun Context.getFragmentManager(): FragmentManager {
    return getBaseContext<AppCompatActivity>().supportFragmentManager
}

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
