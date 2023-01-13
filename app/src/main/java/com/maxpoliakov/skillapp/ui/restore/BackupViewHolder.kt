package com.maxpoliakov.skillapp.ui.restore

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.BackupListItemBinding
import com.maxpoliakov.skillapp.di.SnackbarRoot
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.NetworkUtil
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase.RestorationState
import com.maxpoliakov.skillapp.shared.util.dateTimeFormatter
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.dialog.showDialog
import com.maxpoliakov.skillapp.util.dialog.showSnackbar
import com.maxpoliakov.skillapp.util.dialog.showToast
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Provider

class BackupViewHolder @AssistedInject constructor(
    @Assisted
    binding: BackupListItemBinding,
    private val restoreBackupUseCase: RestoreBackupUseCase,
    private val ioScope: CoroutineScope,
    private val networkUtil: NetworkUtil,
    private val navController: NavController,
    @SnackbarRoot
    private val snackbarRootProvider: Provider<View?>,
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

    private fun restoreBackup() {
        logEvent("restore_backup")
        val backup = backup.value ?: return

        ioScope.launch {
            val result = restoreBackupUseCase.restoreBackup(backup)
            handle(result)
        }
    }

    private suspend fun handle(result: RestoreBackupUseCase.Result) = withContext(Dispatchers.Main) {
        when (result) {
            is RestoreBackupUseCase.Result.Success -> {
                snackbarRootProvider.get()?.showSnackbar(R.string.backup_restore_successful)
                navController.navigateUp()
            }

            is RestoreBackupUseCase.Result.FetchFailure,
            is RestoreBackupUseCase.Result.RestorationFailure -> {
                snackbarRootProvider.get()?.showSnackbar(R.string.something_went_wrong)
            }

            is RestoreBackupUseCase.Result.AlreadyInProgress -> {}
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(binding: BackupListItemBinding): BackupViewHolder
    }
}
