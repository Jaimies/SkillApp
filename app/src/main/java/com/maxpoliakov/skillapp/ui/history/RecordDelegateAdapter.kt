package com.maxpoliakov.skillapp.ui.history

import android.view.View
import android.view.ViewGroup
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.RecordsItemBinding
import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.ui.history.RecordDelegateAdapter.ViewHolder
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding

class RecordDelegateAdapter : DelegateAdapter<Record, ViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        parent.inflateDataBinding<RecordsItemBinding>(R.layout.records_item).run {
            val viewModel = RecordViewModel().also { viewModel = it }
            return ViewHolder(root, viewModel)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Record) {
        holder.bindRecord(item)
    }

    class ViewHolder(
        view: View,
        private val viewModel: RecordViewModel
    ) : BaseViewHolder(view) {

        fun bindRecord(record: Record) {
            viewModel.setRecord(record)
        }
    }
}
