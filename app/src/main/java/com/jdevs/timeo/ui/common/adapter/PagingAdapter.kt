package com.jdevs.timeo.ui.common.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.model.ViewItem

class PagingAdapter(private val delegateAdapter: DelegateAdapter) :
    PagedListAdapter<ViewItem, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapter.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapter.onBindViewHolder(holder, getItem(position))
    }

    public override fun getItem(position: Int) = super.getItem(position)!!
}
