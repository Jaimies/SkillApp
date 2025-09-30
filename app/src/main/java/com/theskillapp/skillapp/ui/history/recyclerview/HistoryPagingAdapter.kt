package com.theskillapp.skillapp.ui.history.recyclerview

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.theskillapp.skillapp.model.HistoryUiModel
import com.theskillapp.skillapp.model.HistoryUiModel.Record
import com.theskillapp.skillapp.shared.recyclerview.adapter.DelegateAdapter
import com.theskillapp.skillapp.ui.history.recyclerview.record.RecordDelegateAdapter
import com.theskillapp.skillapp.ui.history.recyclerview.separator.SeparatorDelegateAdapter
import javax.inject.Inject
import javax.inject.Provider

class HistoryPagingAdapter @Inject constructor(
    private val lifecycleOwnerProvider: Provider<LifecycleOwner>,
    recordDelegateAdapter: RecordDelegateAdapter,
    separatorDelegateAdapter: SeparatorDelegateAdapter,
) : PagingDataAdapter<HistoryUiModel, ViewHolder>(HistoryDiffCallback()) {

    private val delegateAdapters = mapOf(
        ItemType.Record to recordDelegateAdapter,
        ItemType.Separator to separatorDelegateAdapter,
    ) as Map<Int, DelegateAdapter<HistoryUiModel, ViewHolder>>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return delegateAdapters[viewType]!!
            .onCreateViewHolder(parent, lifecycleOwnerProvider.get())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        delegateAdapters[getItemViewType(position)]!!
            .onBindViewHolder(holder, getItem(position)!!)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: return -1

        return when (item) {
            is Record -> ItemType.Record
            is HistoryUiModel.Separator -> ItemType.Separator
        }
    }

    object ItemType {
        const val Record = 0
        const val Separator = 1
    }
}
