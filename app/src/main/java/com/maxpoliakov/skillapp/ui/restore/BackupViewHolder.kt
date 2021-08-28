package com.maxpoliakov.skillapp.ui.restore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.BackupListItemBinding
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.usecase.backup.RestorationState
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class BackupViewHolder(
    binding: BackupListItemBinding,
    private val restoreBackupUseCase: RestoreBackupUseCase,
    private val ioScope: CoroutineScope,
) : RecyclerView.ViewHolder(binding.root) {

    private val _backup = MutableLiveData<Backup>()
    val backup: LiveData<Backup> get() = _backup

    val backupCreationDate = backup.map { backup -> formatter.format(backup.creationDate) }

    init {
        binding.viewHolder = this
    }

    fun setBackup(backup: Backup) {
        _backup.value = backup
    }

    fun requestRestoreBackup() {
        if (restoreBackupUseCase.state.value == RestorationState.Active) return

        MaterialAlertDialogBuilder(itemView.context, R.style.ThemeOverlay_SkillApp_AlertDialog)
            .setMessage(R.string.confirm_restore_backup)
            .setPositiveButton(R.string.restore) { _, _ -> restoreBackup() }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun restoreBackup() = ioScope.launch {
        val backup = backup.value ?: return@launch
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

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy  hh:mm:ss")
    }
}
