package com.maxpoliakov.skillapp.ui.restore

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.BackupListItemBinding
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import javax.inject.Inject

class BackupListAdapter @Inject constructor(
    private val viewHolderFactory: BackupViewHolder.Factory,
) : ListAdapter<Backup, BackupViewHolder>(BackupDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackupViewHolder {
        val binding = parent.inflateDataBinding<BackupListItemBinding>(R.layout.backup_list_item)
        return viewHolderFactory.create(binding)
    }

    override fun onBindViewHolder(holder: BackupViewHolder, position: Int) {
        holder.setBackup(getItem(position))
    }
}
