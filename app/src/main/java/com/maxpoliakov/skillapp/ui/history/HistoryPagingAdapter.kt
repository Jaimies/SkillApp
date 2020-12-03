package com.maxpoliakov.skillapp.ui.history

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maxpoliakov.skillapp.model.HistoryUiModel
import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import javax.inject.Inject

class HistoryPagingAdapter @Inject constructor(
    recordDelegateAdapter: RecordDelegateAdapter
) : PagingDataAdapter<HistoryUiModel, ViewHolder>(HistoryDiffCallback()) {

    private val delegateAdapters = listOf(
        recordDelegateAdapter,
        SeparatorDelegateAdapter()
    ) as List<DelegateAdapter<HistoryUiModel, ViewHolder>>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return delegateAdapters[viewType].onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val delegateAdapter = delegateAdapters[getItemViewType(position)]
        delegateAdapter.onBindViewHolder(holder, getItem(position)!!)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is Record) 0 else 1
    }
}
