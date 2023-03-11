package com.maxpoliakov.skillapp.ui.common.recyclerview.itemdecoration.margin

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.util.ui.Dp

abstract class BottomMarginItemDecoration: RecyclerView.ItemDecoration() {
    abstract val marginByItemViewType: Map<Int, Dp>

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val viewType = parent.adapter?.getItemViewType(position) ?: return
        val margin = marginByItemViewType[viewType] ?: return

        outRect.bottom = margin.toPx(parent.context)
    }
}
