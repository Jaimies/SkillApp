package com.jdevs.timeo.ui.common.adapter

import androidx.recyclerview.widget.DiffUtil
import com.jdevs.timeo.model.ViewItem

class DiffCallback<T : ViewItem> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id == newItem.id
    }

    @Suppress("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}
