package com.theskillapp.skillapp.ui.restore

import androidx.recyclerview.widget.DiffUtil
import com.theskillapp.skillapp.domain.model.Backup

object BackupDiffCallback : DiffUtil.ItemCallback<Backup>() {
    override fun areItemsTheSame(oldItem: Backup, newItem: Backup): Boolean {
        return oldItem.uri == newItem.uri
    }

    override fun areContentsTheSame(oldItem: Backup, newItem: Backup): Boolean {
        return oldItem == newItem
    }
}
