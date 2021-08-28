package com.maxpoliakov.skillapp.ui.restore

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.BackupListItemBinding
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class BackupViewHolder(
    binding: BackupListItemBinding,
    private val restoreBackupUseCase: RestoreBackupUseCase,
    private val ioScope: CoroutineScope,
) : RecyclerView.ViewHolder(binding.root) {
    var backup: Backup? = null
        private set

    init {
        binding.viewHolder = this
    }

    fun setBackup(backup: Backup) {
        this.backup = backup
    }

    fun requestRestoreBackup() {
        MaterialAlertDialogBuilder(itemView.context, R.style.ThemeOverlay_SkillApp_AlertDialog)
            .setMessage(R.string.confirm_restore_backup)
            .setPositiveButton(R.string.restore) { _, _ -> restoreBackup() }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun restoreBackup() = ioScope.launch {
        val backup = backup ?: return@launch
        restoreBackupUseCase.restoreBackup(backup)
    }

    class Factory @Inject constructor(
        private val restoreBackupUseCase: RestoreBackupUseCase,
        private val ioScope: CoroutineScope,
    ) {
        fun create(binding: BackupListItemBinding): BackupViewHolder {
            return BackupViewHolder(binding, restoreBackupUseCase, ioScope)
        }
    }
}
