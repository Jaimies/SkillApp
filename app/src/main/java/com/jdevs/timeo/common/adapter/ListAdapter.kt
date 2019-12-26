package com.jdevs.timeo.common.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class ListAdapter : PagedListAdapter<DataUnit, RecyclerView.ViewHolder>(ITEMS_COMPARATOR) {

    protected abstract val delegateAdapter: DelegateAdapter

    override fun getItemViewType(index: Int) = getItem(index)?.getViewType() ?: -1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        delegateAdapter.onBindViewHolder(holder, getItem(position) ?: return)
    }

    public override fun getItem(position: Int) = super.getItem(position)

    companion object {

        @SuppressLint("DiffUtilEquals")
        private val ITEMS_COMPARATOR = object : DiffUtil.ItemCallback<DataUnit>() {

            override fun areItemsTheSame(oldItem: DataUnit, newItem: DataUnit): Boolean {

                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataUnit, newItem: DataUnit): Boolean {

                return oldItem === newItem
            }
        }
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        protected val context: Context = view.context
        protected val lifecycleOwner = context as LifecycleOwner
    }
}
