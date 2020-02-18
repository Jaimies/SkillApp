package com.jdevs.timeo.ui.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.model.ViewItem

class ListAdapter(private val delegateAdapter: DelegateAdapter) :
    ListAdapter<ViewItem, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegateAdapter.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        delegateAdapter.onBindViewHolder(holder, getItem(position))
    }
}
