package com.maxpoliakov.skillapp.ui.history

import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.RecordsItemBinding
import com.maxpoliakov.skillapp.di.ChildFragmentManager
import com.maxpoliakov.skillapp.model.HistoryUiModel
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder
import com.maxpoliakov.skillapp.util.dialog.showDialog
import com.maxpoliakov.skillapp.util.fragment.showDatePicker
import com.maxpoliakov.skillapp.util.fragment.showTimePicker
import com.maxpoliakov.skillapp.util.ui.dp
import com.maxpoliakov.skillapp.util.ui.increaseTouchAreaBy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class RecordViewHolder @AssistedInject constructor(
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
            R.id.change_start_time -> showChangeStartTimeDialog();
            R.id.change_end_time -> showChangeEndTimeDialog();
        }

        return true
    }

    private fun showPopupMenu(view: View, unit: UiMeasurementUnit): PopupMenu {
        val popup = PopupMenu(context, view, Gravity.END)

        popup.menu.run {
            add(Menu.NONE, R.id.change_date, 0, R.string.change_date)
            add(Menu.NONE, R.id.change_count, 1, unit.changeCountResId)
            add(Menu.NONE, R.id.delete, 4, R.string.delete)

            if (viewModel.record.value?.dateTimeRange != null) {
                add(Menu.NONE, R.id.change_start_time, 2, R.string.change_start_time)
                add(Menu.NONE, R.id.change_end_time, 3, R.string.change_end_time)
            }
        }

        popup.show()
        return popup
    }

    private fun showDeleteDialog() {
        context.showDialog(
            R.string.delete_record_title,
            R.string.delete_record_message,
            R.string.delete,
            viewModel::deleteRecord,
        )
    }

    private fun showChangeDateDialog() {
        fragmentManager.showDatePicker(
            viewModel.record.value!!.date,
            viewModel::changeRecordDate,
        )
    }

    private fun showCountPickerDialog() {
        viewModel.record.value!!.unit.showPicker(
            fragmentManager,
            viewModel.record.value!!.count,
            editMode = true,
            onTimeSet = viewModel::changeRecordTime
        )
    }

    private fun showChangeStartTimeDialog() {
        fragmentManager.showTimePicker(
            context,
            viewModel.record.value?.dateTimeRange?.start ?: return,
            viewModel::changeStartTime,
        )
    }

    private fun showChangeEndTimeDialog() {
        fragmentManager.showTimePicker(
            context,
            viewModel.record.value?.dateTimeRange?.endInclusive ?: return,
            viewModel::changeEndTime,
        )
    }

    fun bindRecord(record: HistoryUiModel.Record) {
        viewModel.setRecord(record)
    }

    @AssistedFactory
    interface Factory {
        fun create(binding: RecordsItemBinding, viewModel: RecordViewModel): RecordViewHolder
    }
}
