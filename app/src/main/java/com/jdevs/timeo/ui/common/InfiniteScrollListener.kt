package com.jdevs.timeo.ui.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InfiniteScrollListener(
    private val visibleThreshold: Int,
    private val onScrolled: () -> Unit
) : RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var isLoading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        if (dy > 0) {
            val visibleItemCount = recyclerView.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

            if (isLoading && totalItemCount > previousTotal) {
                isLoading = false
                previousTotal = totalItemCount
            }

            if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                recyclerView.post { onScrolled() }
                isLoading = true
            }
        }
    }
}
