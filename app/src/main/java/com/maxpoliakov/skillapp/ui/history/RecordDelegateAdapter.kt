package com.maxpoliakov.skillapp.ui.history

import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.RecordsItemBinding
import com.maxpoliakov.skillapp.di.ChildFragmentManager
import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.ui.history.RecordDelegateAdapter.ViewHolder
import com.maxpoliakov.skillapp.util.dialog.showDialog
import com.maxpoliakov.skillapp.util.fragment.showDatePicker
import com.maxpoliakov.skillapp.util.ui.dp
import com.maxpoliakov.skillapp.util.ui.increaseTouchAreaBy
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
import javax.inject.Provider

@FragmentScoped
class RecordDelegateAdapter @Inject constructor(
    private val viewModelProvider: Provider<RecordViewModel>,
    private val viewHolderFactory: ViewHolder.Factory,
) : DelegateAdapter<Record, ViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): ViewHolder {
        parent.inflateDataBinding<RecordsItemBinding>(R.layout.records_item).run {
            this.lifecycleOwner = lifecycleOwner
            val viewModel = viewModelProvider.get().also { viewModel = it }
            return viewHolderFactory.create(this, viewModel)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Record) {
        holder.bindRecord(item)
    }

    class ViewHolder @AssistedInject constructor(
        @Assisted
        private val binding: RecordsItemBinding,
        @Assisted
        private val viewModel: RecordViewModel,
        @ChildFragmentManager
        private val fragmentManager: FragmentManager,
    ) : BaseViewHolder(binding) {

        init {
            binding.moreBtn.increaseTouchAreaBy(35.dp)

            viewModel.showMenu.observe(lifecycleOwner) {
                val popup = showPopupMenu(binding.moreBtn, viewModel.record.value!!.unit)
                popup.setOnMenuItemClickListener(this::onMenuItemClicked)
            }
        }

        private fun onMenuItemClicked(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.delete -> showDeleteDialog()
                R.id.change_date -> showChangeDateDialog()
                R.id.change_count -> showCountPickerDialog()
            }

            return true
        }

        private fun showPopupMenu(view: View, unit: UiMeasurementUnit): PopupMenu {
            val popup = PopupMenu(context, view, Gravity.END)

            popup.menu.run {
                add(Menu.NONE, R.id.change_date, 0, R.string.change_date)
                add(Menu.NONE, R.id.change_count, 1, unit.changeCountResId)
                add(Menu.NONE, R.id.delete, 2, R.string.delete)
            }

            popup.show()
            return popup
        }

        private fun showDeleteDialog() {
            context.showDialog(R.string.delete_record_title, R.string.delete_record_message, R.string.delete) {
                viewModel.deleteRecord()
            }
        }

        private fun showChangeDateDialog() {
            fragmentManager.showDatePicker(viewModel.record.value!!.date) { date ->
                viewModel.changeRecordDate(date)
            }
        }

        private fun showCountPickerDialog() {
            viewModel.record.value!!.unit.showPicker(fragmentManager, viewModel.record.value!!.count, editMode = true) { time ->
                viewModel.changeRecordTime(time)
            }
        }

        fun bindRecord(record: Record) {
            viewModel.setRecord(record)
        }

        @AssistedFactory
        interface Factory {
            fun create(binding: RecordsItemBinding, viewModel: RecordViewModel): ViewHolder
        }
    }
}
