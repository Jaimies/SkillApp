package com.theskillapp.skillapp.ui.history.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.theskillapp.skillapp.model.HistoryUiModel
import com.theskillapp.skillapp.model.HistoryUiModel.Record
import com.theskillapp.skillapp.model.HistoryUiModel.Separator

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
