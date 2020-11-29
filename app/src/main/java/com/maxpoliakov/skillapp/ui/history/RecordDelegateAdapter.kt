package com.maxpoliakov.skillapp.ui.history

import android.view.View
import android.view.ViewGroup
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.RecordsItemBinding
import com.maxpoliakov.skillapp.model.RecordItem
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding

class RecordDelegateAdapter(
    private val showDeleteDialog: (index: Int) -> Unit
) : DelegateAdapter<RecordItem, RecordDelegateAdapter.ViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        parent.inflateDataBinding<RecordsItemBinding>(R.layout.records_item).run {
            val viewModel = RecordViewModel().also { viewModel = it }
            return ViewHolder(root, viewModel, showDeleteDialog)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, item: RecordItem) {
        holder.bindRecord(item)
    }

    class ViewHolder(
        view: View,
        private val viewModel: RecordViewModel,
        showDeleteDialog: (index: Int) -> Unit
    ) : BaseViewHolder(view) {

        init {
            viewModel.showDeleteDialog.observe { showDeleteDialog(adapterPosition) }
        }

        fun bindRecord(record: RecordItem) {
            viewModel.setRecord(record)
        }
    }
}