package com.jdevs.timeo.util.view

import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View =
    LayoutInflater.from(context).inflate(layoutId, this, false)

val ViewGroup.fragmentActivity get() = context.getBaseContext() as FragmentActivity

inline fun <reified T> Context.getBaseContext() =
    if (this is T) this else (this as ContextWrapper).baseContext as T

fun RecyclerView.setupAdapter(
    adapter: RecyclerView.Adapter<*>,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL
) {

    if (layoutManager != null && this.adapter != null) {
        return
    }

    layoutManager = LinearLayoutManager(context, orientation, false)
    this.adapter = adapter
}
