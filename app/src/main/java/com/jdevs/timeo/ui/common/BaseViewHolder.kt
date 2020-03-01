package com.jdevs.timeo.ui.common

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.util.view.getBaseContext

abstract class BaseViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    protected val context: Context get() = view.context
    protected val lifecycleOwner get() = context.getBaseContext() as LifecycleOwner

    protected inline fun <T> LiveData<T>.observe(crossinline onChanged: (T) -> Unit) {

        observe(lifecycleOwner) { onChanged(it) }
    }
}
