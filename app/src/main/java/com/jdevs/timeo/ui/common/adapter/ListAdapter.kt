package com.jdevs.timeo.ui.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.model.ViewItem

class ListAdapter<T: ViewItem, VH: RecyclerView.ViewHolder>(
    private val delegateAdapter: DelegateAdapter<T, VH>
) : ListAdapter<T, VH>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return delegateAdapter.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        delegateAdapter.onBindViewHolder(holder, getItem(position))
    }

    public override fun getItem(position: Int): T {
        return super.getItem(position)
    }
}
