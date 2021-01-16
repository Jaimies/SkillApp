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
import com.maxpoliakov.skillapp.util.fragment.showDatePicker
import com.maxpoliakov.skillapp.util.fragment.showTimePicker
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import com.maxpoliakov.skillapp.util.ui.showPopupMenu
import kotlinx.android.synthetic.main.records_item.view.more_btn
import javax.inject.Inject
import javax.inject.Provider

class RecordDelegateAdapter @Inject constructor(
    private val viewModelProvider: Provider<RecordViewModel>
) : DelegateAdapter<Record, ViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        parent.inflateDataBinding<RecordsItemBinding>(R.layout.records_item).run {
            val viewModel = viewModelProvider.get().also { viewModel = it }
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
            when (menuItem.itemId) {
                R.id.delete -> showDeleteDialog()
                R.id.change_date -> showChangeDateDialog()
                R.id.change_time -> showTimePickerDialog()
            }

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

        private fun showChangeDateDialog() {
            context.showDatePicker(viewModel.record.value!!.date) { date ->
                viewModel.changeRecordDate(date)
            }
        }

        private fun showTimePickerDialog() {
            context.showTimePicker(viewModel.record.value!!.time) { time ->
                viewModel.changeRecordTime(time)
            }
        }

        fun bindRecord(record: Record) {
            viewModel.setRecord(record)
        }
    }
}
