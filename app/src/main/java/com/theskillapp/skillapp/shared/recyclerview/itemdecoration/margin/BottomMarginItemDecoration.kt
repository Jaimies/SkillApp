package com.theskillapp.skillapp.shared.recyclerview.itemdecoration.margin

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.theskillapp.skillapp.shared.Dimension

abstract class BottomMarginItemDecoration: RecyclerView.ItemDecoration() {
    abstract val marginByItemViewType: Map<Int, Dimension>

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val viewType = parent.adapter?.getItemViewType(position) ?: return
        val margin = marginByItemViewType[viewType] ?: return

        outRect.bottom = margin.toPx(parent.context)
    }
}
