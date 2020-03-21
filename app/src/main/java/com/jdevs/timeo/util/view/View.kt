package com.jdevs.timeo.util.view

import android.content.Context
import android.content.ContextWrapper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View =
    LayoutInflater.from(context).inflate(layoutId, this, false)

val ViewGroup.fragmentActivity get() = context.getBaseContext() as FragmentActivity

inline fun <reified T> Context.getBaseContext() =
    if (this is T) this else (this as ContextWrapper).baseContext as T

const val HAS_TEXT_WATCHER = "HAS_TEXT_WATCHER"

inline fun EditText.doOnceAfterTextChanged(crossinline block: () -> Unit) {

    if (tag == HAS_TEXT_WATCHER) return

    addTextChangedListener(object : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {

            block()
            tag = ""
            removeTextChangedListener(this)
        }
    })

    tag = HAS_TEXT_WATCHER
}

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