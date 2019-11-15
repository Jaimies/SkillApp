package com.jdevs.timeo.models

import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollDownListener(private val callback: () -> Unit = {}) : RecyclerView.OnScrollListener() {

    private var isScrolling = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

        super.onScrollStateChanged(recyclerView, newState)

        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

            isScrolling = true
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        (recyclerView.layoutManager as? LinearLayoutManager)?.apply {

            val lastVisibleItem = findLastVisibleItemPosition()
            val totalItemsCount = itemCount

            if (isScrolling && (lastVisibleItem == totalItemsCount - 1)) {

                isScrolling = false
                callback()
            }
        }
    }
}
