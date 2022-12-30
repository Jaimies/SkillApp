package com.maxpoliakov.skillapp.ui.history

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maxpoliakov.skillapp.model.HistoryUiModel
import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.ui.common.LifecycleOwnerProvider
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class HistoryPagingAdapter @AssistedInject constructor(
    @Assisted
    private val lifecycleOwnerProvider: LifecycleOwnerProvider,
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

    @AssistedFactory
    interface Factory {
        fun create(lifecycleOwnerProvider: LifecycleOwnerProvider): HistoryPagingAdapter
    }
}
