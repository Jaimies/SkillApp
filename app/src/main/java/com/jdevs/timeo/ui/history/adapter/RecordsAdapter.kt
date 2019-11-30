package com.jdevs.timeo.ui.history.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.common.adapter.BaseAdapter
import com.jdevs.timeo.util.AdapterConstants.RECORD

class RecordsAdapter(
    private val showDeleteDialog: (Int) -> Unit = {}
) : BaseAdapter() {

    init {
        delegateAdapters.put(RECORD, RecordDelegateAdapter())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegateAdapters.get(viewType)
            .onCreateViewHolder(parent, deleteRecord = showDeleteDialog)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position))
            .onBindViewHolder(holder, items[position])
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()
    override fun getItemCount() = items.size
}
