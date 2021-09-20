package com.maxpoliakov.skillapp.util.ui

import androidx.recyclerview.widget.RecyclerView

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
