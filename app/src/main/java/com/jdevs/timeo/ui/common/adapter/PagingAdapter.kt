package com.jdevs.timeo.ui.common.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.util.getBaseContext

class PagingAdapter(private val delegateAdapter: DelegateAdapter) :
    PagedListAdapter<ViewItem, RecyclerView.ViewHolder>(DiffCallback) {

    override fun getItemViewType(index: Int) = getItem(index)?.viewType ?: -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegateAdapter.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        delegateAdapter.onBindViewHolder(holder, getItem(position) ?: return)
    }

    public override fun getItem(position: Int) = super.getItem(position)

    abstract class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        protected val context: Context get() = view.context
        protected val lifecycleOwner get() = context.getBaseContext() as LifecycleOwner
    }
}
