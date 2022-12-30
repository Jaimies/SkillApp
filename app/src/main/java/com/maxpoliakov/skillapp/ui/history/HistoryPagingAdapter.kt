package com.maxpoliakov.skillapp.ui.history

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maxpoliakov.skillapp.model.HistoryUiModel
import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import javax.inject.Inject
import javax.inject.Provider

class HistoryPagingAdapter @Inject constructor(
    private val lifecycleOwnerProvider: Provider<LifecycleOwner>,
    recordDelegateAdapter: RecordDelegateAdapter
) : PagingDataAdapter<HistoryUiModel, ViewHolder>(HistoryDiffCallback()) {

    private val delegateAdapters = mapOf(
        ItemType.Record to recordDelegateAdapter,
        ItemType.Separator to SeparatorDelegateAdapter()
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
