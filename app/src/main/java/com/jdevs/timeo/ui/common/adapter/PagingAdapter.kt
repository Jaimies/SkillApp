package com.jdevs.timeo.ui.common.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class PagingAdapter(
    private val delegateAdapter: DelegateAdapter,
    private val createRecord: (Int, Long) -> Unit = { _, _ -> },
    private val navigateToDetails: (Int) -> Unit = {},
    private val showDeleteDialog: (Int) -> Unit = {}
) : PagedListAdapter<ViewItem, RecyclerView.ViewHolder>(ITEMS_COMPARATOR) {

    override fun getItemViewType(index: Int) = getItem(index)?.viewType ?: -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = delegateAdapter
        .onCreateViewHolder(parent, createRecord, navigateToDetails, showDeleteDialog)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        delegateAdapter.onBindViewHolder(holder, getItem(position) ?: return)
    }

    public override fun getItem(position: Int) = super.getItem(position)

    companion object {

        @SuppressLint("DiffUtilEquals")
        private val ITEMS_COMPARATOR = object : DiffUtil.ItemCallback<ViewItem>() {

            override fun areItemsTheSame(oldItem: ViewItem, newItem: ViewItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ViewItem, newItem: ViewItem) =
                oldItem === newItem
        }
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        protected val context: Context = view.context
        protected val lifecycleOwner = context as LifecycleOwner
    }
}
