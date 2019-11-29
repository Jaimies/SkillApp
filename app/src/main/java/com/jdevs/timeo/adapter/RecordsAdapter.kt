package com.jdevs.timeo.adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.adapter.delegates.LoadingDelegateAdapter
import com.jdevs.timeo.adapter.delegates.RecordDelegateAdapter
import com.jdevs.timeo.adapter.delegates.ViewType
import com.jdevs.timeo.adapter.delegates.ViewTypeDelegateAdapter
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.util.AdapterConstants.LOADING
import com.jdevs.timeo.util.AdapterConstants.RECORD

class RecordsAdapter(
    private val showDeleteDialog: (Int) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLastItemReached = false
    private val recordsList = ArrayList<ViewType>()
    private val delegateAdapters = SparseArray<ViewTypeDelegateAdapter>()

    private val loadingItem = object : ViewType {

        override fun getViewType() = LOADING
    }

    init {
        delegateAdapters.put(LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(RECORD, RecordDelegateAdapter())

        recordsList.add(loadingItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegateAdapters.get(viewType)
            .onCreateViewHolder(parent, deleteRecord = showDeleteDialog)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position))
            .onBindViewHolder(holder, recordsList[position])
    }

    override fun getItemViewType(position: Int) = recordsList[position].getViewType()

    override fun getItemCount() = recordsList.size

    fun addItem(item: Record) = recordsList.apply {
        val initPosition = size - 1

        removeAt(initPosition)
        notifyItemRemoved(initPosition)

        add(item)

        if (!isLastItemReached) {

            add(loadingItem)
        }

        notifyItemRangeInserted(initPosition, size - 1)
    }

    fun modifyItem(index: Int, item: Record) {

        recordsList[index] = item
        notifyItemChanged(index)
    }

    fun removeItem(index: Int) {
        recordsList.removeAt(index)
        notifyItemRemoved(index)
    }

    fun onLastItemReached() {

        isLastItemReached = true
        recordsList.removeAt(recordsList.lastIndex)
    }


    fun removeAllItems() {

        recordsList.clear()
        recordsList.add(loadingItem)

        notifyDataSetChanged()
    }
}