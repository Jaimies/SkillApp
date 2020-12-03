package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import android.content.ContextWrapper
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
