package com.jdevs.timeo.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.util.ActivitiesConstants

class InfiniteScrollListener(
    private val func: () -> Unit,
    private val layoutManager: LinearLayoutManager,
    private val visibleThreshold: Int = ActivitiesConstants.VISIBLE_THRESHOLD
) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var isLoading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy > 0) {

            val visibleItemCount = recyclerView.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

            if (isLoading) {
                if (totalItemCount > previousTotal) {
                    isLoading = false
                    previousTotal = totalItemCount
                }
            }

            if (!isLoading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)
            ) {
                func()
                isLoading = true
            }
        }
    }
}
