package com.jdevs.timeo.util.extensions

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

@Suppress("EmptyFunctionBlock")
fun EditText.doOnceAfterTextChanged(block: () -> Unit) {

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

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View =
    LayoutInflater.from(context).inflate(layoutId, this, false)

fun ViewGroup.getFragmentActivity(): FragmentActivity = context.getBaseContext()

inline fun <reified T> Context.getBaseContext() =
    if (this is T) this else (this as ContextWrapper).baseContext as T

fun RecyclerView.setupAdapter(
    adapter: RecyclerView.Adapter<*>,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL
) {

    layoutManager = LinearLayoutManager(context, orientation, false)
    this.adapter = adapter
}

private const val HAS_TEXT_WATCHER = "HAS_TEXT_WATCHER"
