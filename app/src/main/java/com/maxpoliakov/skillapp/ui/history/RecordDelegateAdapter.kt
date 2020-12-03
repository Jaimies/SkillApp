package com.maxpoliakov.skillapp.ui.history

import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.RecordsItemBinding
import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.ui.history.RecordDelegateAdapter.ViewHolder
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import com.maxpoliakov.skillapp.util.ui.showPopupMenu
import kotlinx.android.synthetic.main.records_item.view.more_btn
import javax.inject.Inject

class RecordDelegateAdapter @Inject constructor(
    private val viewModelFactory: RecordViewModel.Factory
) : DelegateAdapter<Record, ViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        parent.inflateDataBinding<RecordsItemBinding>(R.layout.records_item).run {
            val viewModel = viewModelFactory.create().also { viewModel = it }
            return ViewHolder(root, viewModel)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Record) {
        holder.bindRecord(item)
    }

    class ViewHolder(
        private val view: View,
        private val viewModel: RecordViewModel
    ) : BaseViewHolder(view) {

        init {
            viewModel.showMenu.observe {
                val popup = showPopupMenu(context, view.more_btn, R.menu.record_item_menu)
                popup.setOnMenuItemClickListener(this::onMenuItemClicked)
            }
        }

        private fun onMenuItemClicked(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.delete)
                showDeleteDialog()

            return true
        }

        private fun showDeleteDialog() {
            MaterialAlertDialogBuilder(context)
                .setTitle(R.string.delete_record_title)
                .setMessage(R.string.delete_record_message)
                .setPositiveButton(R.string.delete) { _, _ -> viewModel.deleteRecord() }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }

        fun bindRecord(record: Record) {
            viewModel.setRecord(record)
        }
    }
}
