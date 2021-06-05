package com.maxpoliakov.skillapp.util.ui

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

private typealias MoveItem = (adapter: RecyclerView.Adapter<*>, from: Int, to: Int) -> kotlin.Unit

fun createDraggingItemTouchHelper(moveItem: MoveItem): ItemTouchHelper {
    val simpleItemTouchCallback =
        object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val adapter = recyclerView.adapter!!
                val from = viewHolder.absoluteAdapterPosition
                val to = target.absoluteAdapterPosition
                moveItem(adapter, from, to)
                adapter.notifyItemMoved(from, to)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        }
    return ItemTouchHelper(simpleItemTouchCallback)
}
