package com.jdevs.timeo.ui.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.model.ViewItem

class ListAdapter<T : ViewItem>(private val delegateAdapter: DelegateAdapter) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        delegateAdapter.onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        delegateAdapter.onBindViewHolder(holder, items[position])

    fun setItems(newItems: List<T>) {

        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size
}
