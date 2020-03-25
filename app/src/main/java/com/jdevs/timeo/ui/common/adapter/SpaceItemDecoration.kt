package com.jdevs.timeo.ui.common.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.util.hardware.dpToPx

class SpaceItemDecoration(private val offset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = parent.context.dpToPx(offset)

        if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
            outRect.right = parent.context.dpToPx(offset)
        }
    }
}
