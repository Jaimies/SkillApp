package com.jdevs.timeo.common.adapter

import android.util.SparseArray
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.ui.activities.ActivityDelegateAdapter
import com.jdevs.timeo.ui.history.RecordDelegateAdapter
import com.jdevs.timeo.util.AdapterConstants

abstract class FirestoreListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val dataItemCount get() = items.filter { it.viewType != AdapterConstants.LOADING }.size

    protected val delegateAdapters = SparseArray<DelegateAdapter>()
    private val items = mutableListOf<ViewItem>()

    private val loadingItem = object : ViewItem {

        override var id = -1
        override var documentId = ""
        override val viewType = AdapterConstants.LOADING
    }

    init {

        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(AdapterConstants.ACTIVITY, ActivityDelegateAdapter())
        delegateAdapters.put(AdapterConstants.RECORD, RecordDelegateAdapter())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        delegateAdapters.get(getItemViewType(position))
            .onBindViewHolder(holder, items[position])
    }

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = items[position].viewType

    fun getItem(position: Int) = items[position]

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

    fun addItem(item: ViewItem) = items.apply {

        if (items.count { it.documentId == item.documentId } > 0) {

            modifyItem(item)
            return@apply
        }

        add(item)

        notifyItemInserted(lastIndex)
    }

    fun modifyItem(item: ViewItem) {

        val index = items.indexOfFirst { it.documentId == item.documentId }

        if (items[index] != item) {

            items[index] = item
            notifyItemChanged(index)
        }
    }

    fun removeItem(item: ViewItem) {

        val index = items.indexOfFirst { it.documentId == item.documentId }

        items.removeAt(index)
        notifyItemRemoved(index)
    }
}
