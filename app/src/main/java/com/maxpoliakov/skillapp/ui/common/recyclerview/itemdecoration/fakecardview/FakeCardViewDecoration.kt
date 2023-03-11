package com.maxpoliakov.skillapp.ui.common.recyclerview.itemdecoration.fakecardview

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

abstract class FakeCardViewDecoration : ItemDecoration() {
    protected abstract val cardFooterViewType: Int

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val drawnGroupIds = mutableListOf<Int>()

        for (i in 0 until parent.childCount) {
            val viewHolder = parent.getChildViewHolder(parent.getChildAt(i))

            if (viewHolder !is PartOfFakeCardView || viewHolder.cardId == -1
                || drawnGroupIds.contains(viewHolder.cardId)) continue

            drawnGroupIds.add(viewHolder.cardId)

            val lastViewHolder = parent.findLastViewHolderInGroup(viewHolder.cardId) ?: continue

            FakeCardViewDrawer(parent.context, c, viewHolder, lastViewHolder, cardFooterViewType).draw()
        }
    }

    private fun RecyclerView.findLastViewHolderInGroup(cardId: Int): RecyclerView.ViewHolder? {
        for (i in childCount - 1 downTo 0) {
            val holder = getChildViewHolder(getChildAt(i))
            if (holder is PartOfFakeCardView && holder.cardId == cardId) return holder
        }

        return null
    }
}
