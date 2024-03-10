package com.maxpoliakov.skillapp.ui.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BackupViewModel(
    private val performBackupUseCase: PerformBackupUseCase,
    backupRepository: BackupRepository,
    private val scope: CoroutineScope,
) : ViewModel() {

    val lastBackupState = backupRepository
        .getLastBackupFlow()
        .map { result ->
            if (result is BackupRepository.Result.Success) LoadingState.Success(null)
            else LoadingState.Error
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, LoadingState.Loading)

    private val _isCreatingBackup = MutableStateFlow(false)
    val isCreatingBackup = _isCreatingBackup.asStateFlow()

    private val _goToRestoreBackupScreen = SingleLiveEventWithoutData()
    val goToRestoreBackupScreen: LiveData<Unit> get() = _goToRestoreBackupScreen

    private val _showSnackbar = SingleLiveEvent<Int>()
    val showSnackbar: LiveData<Int> get() = _showSnackbar

    abstract val isConfigured: Flow<Boolean>

    abstract fun onAttemptedToGoToRestoreBackupScreenWhenNotConfigured()

    fun createBackup() = scope.launch {
        if (isConfigured.first()) return@launch

        _isCreatingBackup.value = true
        val result = performBackupUseCase.performBackup()
        handleBackupCreationResult(result)
        _isCreatingBackup.value = false
    }

    open fun handleBackupCreationResult(result: PerformBackupUseCase.Result) {
        when (result) {
            is PerformBackupUseCase.Result.Success -> {
                showSnackbar(R.string.backup_successful)
            }

            is PerformBackupUseCase.Result.CreationFailure -> {
                showSnackbar(R.string.backup_creation_failed)
            }

            is PerformBackupUseCase.Result.UploadFailure -> handleUploadFailure(result)
        }
    }

    open fun handleUploadFailure(result: PerformBackupUseCase.Result.UploadFailure) {
        showSnackbar(R.string.backup_upload_failed)
    }

    fun goToRestore() = viewModelScope.launch {
        if (!isConfigured.first()) onAttemptedToGoToRestoreBackupScreenWhenNotConfigured()
        else _goToRestoreBackupScreen.call()
    }

    protected fun showSnackbar(messageStringResId: Int) {
        _showSnackbar.value = messageStringResId
    }

    sealed class LoadingState {
        object Loading : LoadingState()
        object Error : LoadingState()

        data class Success(val backup: Backup?) : LoadingState()
    }
}
