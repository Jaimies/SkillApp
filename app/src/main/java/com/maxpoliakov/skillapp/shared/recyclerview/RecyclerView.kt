package com.maxpoliakov.skillapp.shared.recyclerview

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.R

fun RecyclerView.setupAdapter(adapter: RecyclerView.Adapter<*>) {
    if (layoutManager != null && this.adapter != null)
        return

    layoutManager = LinearLayoutManager(context)
    this.adapter = adapter
}


inline fun <reified T : RecyclerView.ViewHolder> RecyclerView.findViewHolder(predicate: (T) -> Boolean): T? {
    for (i in 0 until adapter!!.itemCount) {
        val viewHolder = findViewHolderForAdapterPosition(i)
        if (viewHolder is T && predicate(viewHolder)) return viewHolder
    }

    return null
}

fun RecyclerView.smoothScrollToTop() {
    post {
        smoothScrollToPosition(0)
    }
}

fun RecyclerView.addDividers() {
    val layoutManager = layoutManager as? LinearLayoutManager ?: return

    val divider = DividerItemDecoration(context, layoutManager.orientation).apply {
        setDrawable(ContextCompat.getDrawable(context, R.drawable.separator)!!)
    }

    addItemDecoration(divider)
}
