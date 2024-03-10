package com.maxpoliakov.skillapp.ui.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    val backupResult: LiveData<PerformBackupUseCase.Result> get() = _backupResult
    private val _backupResult = SingleLiveEvent<PerformBackupUseCase.Result>()

    abstract val isConfigured: Flow<Boolean>

    abstract fun onAttemptedToGoToRestoreBackupScreenWhenNotConfigured()

    fun createBackup() = scope.launch {
        if (isConfigured.first()) return@launch

        _isCreatingBackup.value = true
        val result = performBackupUseCase.performBackup()
        _backupResult.value = result
        onBackupAttempted(result)
        _isCreatingBackup.value = false
    }

    open fun onBackupAttempted(result: PerformBackupUseCase.Result) {}

    fun goToRestore() = viewModelScope.launch {
        if (!isConfigured.first()) onAttemptedToGoToRestoreBackupScreenWhenNotConfigured()
        else _goToRestoreBackupScreen.call()
    }

    sealed class LoadingState {
        object Loading : LoadingState()
        object Error : LoadingState()

        data class Success(val backup: Backup?) : LoadingState()
    }
}
