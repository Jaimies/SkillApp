package com.maxpoliakov.skillapp.ui.restore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.BackupListItemBinding
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.usecase.backup.RestorationState
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.dialog.showDialog
import com.maxpoliakov.skillapp.util.dialog.showSnackbar
import com.maxpoliakov.skillapp.util.dialog.showToast
import com.maxpoliakov.skillapp.util.error.logToCrashlytics
import com.maxpoliakov.skillapp.util.network.NetworkUtil
import com.maxpoliakov.skillapp.util.time.dateTimeFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class BackupViewHolder(
    binding: BackupListItemBinding,
    private val restoreBackupUseCase: RestoreBackupUseCase,
    private val ioScope: CoroutineScope,
    private val networkUtil: NetworkUtil,
) : RecyclerView.ViewHolder(binding.root) {

    private val _backup = MutableLiveData<Backup>()
    val backup: LiveData<Backup> get() = _backup

    val backupCreationDate = backup.map { backup -> dateTimeFormatter.format(backup.creationDate) }

    init {
        binding.viewHolder = this
    }

    fun setBackup(backup: Backup) {
        _backup.value = backup
    }

    fun requestRestoreBackup() {
        if (restoreBackupUseCase.state.value == RestorationState.Active) return

        itemView.context.showDialog(R.string.confirm_restore_backup, R.string.restore) {
            if (!networkUtil.isConnected)
                itemView.context.showToast(R.string.no_internet)
            else
                restoreBackup()
        }
    }

    private fun restoreBackup() = ioScope.launch {
        logEvent("restore_backup")
        val backup = backup.value ?: return@launch
        try {
            restoreBackupUseCase.restoreBackup(backup)
        } catch (e: Exception) {
            e.logToCrashlytics()
            e.printStackTrace()
            itemView.showSnackbar(R.string.something_went_wrong)
        }
    }

    class Factory @Inject constructor(
        private val restoreBackupUseCase: RestoreBackupUseCase,
        private val ioScope: CoroutineScope,
        private val networkUtil: NetworkUtil,
    ) {
        fun create(binding: BackupListItemBinding): BackupViewHolder {
            return BackupViewHolder(binding, restoreBackupUseCase, ioScope, networkUtil)
        }
    }
}
