package com.jdevs.timeo.common.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.SparseArray
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.ui.activities.ActivityDelegateAdapter
import com.jdevs.timeo.ui.history.RecordDelegateAdapter
import com.jdevs.timeo.util.AdapterConstants.ACTIVITY
import com.jdevs.timeo.util.AdapterConstants.LOADING
import com.jdevs.timeo.util.AdapterConstants.RECORD

abstract class ListAdapter : PagedListAdapter<DataUnit, RecyclerView.ViewHolder>(ITEMS_COMPARATOR) {

    val dataItemCount get() = items.filter { it.getViewType() != LOADING }.size

    protected val delegateAdapters = SparseArray<DelegateAdapter>()
    private val items = mutableListOf<DataUnit>()

    private val loadingItem = object : DataUnit {

        override var id = -1
        override var documentId = ""
        override fun getViewType() = LOADING
    }

    init {

        delegateAdapters.put(LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(ACTIVITY, ActivityDelegateAdapter())
        delegateAdapters.put(RECORD, RecordDelegateAdapter())
//        items.add(loadingItem)
    }

    override fun getItemViewType(index: Int) = getItem(index)?.getViewType() ?: -1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        delegateAdapters.get(getItemViewType(position))
            ?.onBindViewHolder(holder, getItem(position) ?: return)
    }

    fun getDataItem(index: Int) = items[index]

    fun showLoader() {

        hideLoader()

        items.add(loadingItem)
        notifyItemInserted(items.lastIndex)
    }

    fun hideLoader() {

        val index = items.indexOf(loadingItem)

        if (index != -1) {

            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun addItem(item: DataUnit) = items.apply {

        if (items.count { it.documentId == item.documentId } > 0) {

            modifyItem(item)
            return@apply
        }

        add(item)

        notifyItemInserted(lastIndex)
    }

    fun modifyItem(item: DataUnit) {

        val index = items.indexOfFirst { it.documentId == item.documentId }

        if (items[index] != item) {

            items[index] = item
            notifyItemChanged(index)
        }
    }

    fun removeItem(item: DataUnit) {

        val index = items.indexOfFirst { it.documentId == item.documentId }

        items.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun submitList(pagedList: PagedList<DataUnit>?) {
        super.submitList(pagedList)

        items.clear()

        pagedList?.forEach {

            if (it != null) {

                items.add(it)
            }
        }
    }

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
