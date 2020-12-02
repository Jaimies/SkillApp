package com.maxpoliakov.skillapp.ui.history

import androidx.recyclerview.widget.DiffUtil
import com.maxpoliakov.skillapp.model.HistoryUiModel
import com.maxpoliakov.skillapp.model.HistoryUiModel.Record
import com.maxpoliakov.skillapp.model.HistoryUiModel.Separator

class HistoryDiffCallback : DiffUtil.ItemCallback<HistoryUiModel>() {
    override fun areItemsTheSame(oldItem: HistoryUiModel, newItem: HistoryUiModel): Boolean {
        if (oldItem is Record && newItem is Record) return oldItem.id == newItem.id
        if (oldItem is Separator && newItem is Separator) return oldItem.date == newItem.date
        return false
    }

    override fun areContentsTheSame(oldItem: HistoryUiModel, newItem: HistoryUiModel): Boolean {
        return oldItem == newItem
    }
}
