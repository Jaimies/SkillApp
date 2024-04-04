package com.maxpoliakov.skillapp.ui.restore

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.BackupListItemBinding
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import com.maxpoliakov.skillapp.shared.extensions.inflateDataBinding
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Provider

class BackupListAdapter @AssistedInject constructor(
    @Assisted
    private val restoreBackupUseCase: RestoreBackupUseCase,
    private val viewHolderFactory: BackupViewHolder.Factory,
    private val lifecycleOwnerProvider: Provider<LifecycleOwner>,
) : ListAdapter<Backup, BackupViewHolder>(BackupDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackupViewHolder {
        val binding = parent.inflateDataBinding<BackupListItemBinding>(R.layout.backup_list_item).apply {
            lifecycleOwner = lifecycleOwnerProvider.get()
        }
        return viewHolderFactory.create(binding, restoreBackupUseCase)
    }

    override fun onBindViewHolder(holder: BackupViewHolder, position: Int) {
        holder.setBackup(getItem(position))
    }

    @AssistedFactory
    interface Factory {
        fun create(restoreBackupUseCase: RestoreBackupUseCase): BackupListAdapter
    }
}
