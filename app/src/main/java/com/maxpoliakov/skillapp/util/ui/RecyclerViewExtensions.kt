package com.maxpoliakov.skillapp.util.ui

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.R

fun RecyclerView.addDividers() {
    val layoutManager = layoutManager as? LinearLayoutManager ?: return

    val divider = DividerItemDecoration(context, layoutManager.orientation).apply {
        setDrawable(ContextCompat.getDrawable(context, R.drawable.separator)!!)
    }

    addItemDecoration(divider)
}

fun RecyclerView.Adapter<*>.setOnItemAddedListener(listener: () -> kotlin.Unit) {
    registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (itemCount == 1) listener()
        }
    })
}
