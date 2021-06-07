package com.maxpoliakov.skillapp.util.ui

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperCallback {
    fun onMove(from: Int, to: Int)
    fun onDropped()
}

fun createDraggingItemTouchHelper(
    callback: ItemTouchHelperCallback
): ItemTouchHelper {

    val simpleItemTouchCallback = object : SimpleCallback(UP or DOWN, 0) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {

            val from = viewHolder.absoluteAdapterPosition
            val to = target.absoluteAdapterPosition
            callback.onMove(from, to)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            callback.onDropped()
        }
    }

    return ItemTouchHelper(simpleItemTouchCallback)
}
