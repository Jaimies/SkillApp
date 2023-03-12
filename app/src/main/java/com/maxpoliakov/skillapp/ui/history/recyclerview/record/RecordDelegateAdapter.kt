package com.maxpoliakov.skillapp.ui.history.recyclerview.record

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.RecordsItemBinding
import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.ui.common.recyclerview.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
import javax.inject.Provider

@FragmentScoped
class RecordDelegateAdapter @Inject constructor(
    private val viewModelProvider: Provider<RecordViewModel>,
    private val viewHolderFactory: RecordViewHolder.Factory,
) : DelegateAdapter<Record, RecordViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): RecordViewHolder {
        parent.inflateDataBinding<RecordsItemBinding>(R.layout.records_item).run {
            this.lifecycleOwner = lifecycleOwner
            val viewModel = viewModelProvider.get().also { viewModel = it }
            return viewHolderFactory.create(this, viewModel)
        }
    }

    override fun onBindViewHolder(holder: RecordViewHolder, item: Record) {
        holder.bindRecord(item)
    }
}
