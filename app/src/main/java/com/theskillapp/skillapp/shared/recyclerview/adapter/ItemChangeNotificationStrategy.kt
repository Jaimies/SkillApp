package com.theskillapp.skillapp.shared.recyclerview.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

sealed class ItemChangeNotificationStrategy {
    abstract fun notifyOfItemChange(listAdapter: RecyclerView.Adapter<ViewHolder>, position: Int)

    object NotifyItemChanged : ItemChangeNotificationStrategy() {
        override fun notifyOfItemChange(listAdapter: RecyclerView.Adapter<ViewHolder>, position: Int) {
            listAdapter.notifyItemChanged(position)
        }
    }

    class OnBindViewHolder(private val recyclerView: RecyclerView?) : ItemChangeNotificationStrategy() {
        override fun notifyOfItemChange(listAdapter: RecyclerView.Adapter<ViewHolder>, position: Int) {
            val viewHolder = recyclerView?.findViewHolderForAdapterPosition(position) ?: return
            listAdapter.onBindViewHolder(viewHolder, position)
        }
    }
}
