package com.jdevs.timeo.ui.common.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.jdevs.timeo.model.ViewItem

internal object DiffCallback : DiffUtil.ItemCallback<ViewItem>() {

    override fun areItemsTheSame(oldItem: ViewItem, newItem: ViewItem): Boolean {

        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ViewItem, newItem: ViewItem): Boolean {

        return oldItem === newItem
    }
}
