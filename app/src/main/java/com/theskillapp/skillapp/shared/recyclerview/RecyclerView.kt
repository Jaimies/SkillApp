package com.theskillapp.skillapp.shared.recyclerview

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.theskillapp.skillapp.R

fun RecyclerView.setupAdapter(adapter: RecyclerView.Adapter<*>) {
    if (layoutManager != null && this.adapter != null)
        return

    layoutManager = LinearLayoutManager(context)
    this.adapter = adapter
}


fun RecyclerView.scrollToTop() {
    post {
        scrollToPosition(0)
    }
}

fun RecyclerView.addDividers() {
    val layoutManager = layoutManager as? LinearLayoutManager ?: return

    val divider = DividerItemDecoration(context, layoutManager.orientation).apply {
        setDrawable(ContextCompat.getDrawable(context, R.drawable.separator)!!)
    }

    addItemDecoration(divider)
}
