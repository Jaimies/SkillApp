package com.theskillapp.skillapp.ui.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theskillapp.skillapp.data.backup.BackupConfigurationManager
import com.theskillapp.skillapp.data.backup.BackupConfigurationManager.Configuration
import com.theskillapp.skillapp.domain.di.ApplicationScope
import com.theskillapp.skillapp.domain.repository.BackupRepository
import com.theskillapp.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.theskillapp.skillapp.model.LoadingState
import com.theskillapp.skillapp.shared.lifecycle.SingleLiveEvent
import com.theskillapp.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BackupViewModel(
    backupRepository: BackupRepository,
    configurationManager: BackupConfigurationManager,
    protected val performBackupUseCase: PerformBackupUseCase,
) : ViewModel() {
    @Inject
    @ApplicationScope
    lateinit var scope: CoroutineScope

    val lastBackupState = backupRepository
        .getLastBackupFlow()
        .map { result ->
            if (result is BackupRepository.Result.Success) LoadingState.Success(result.value)
            else LoadingState.Error
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, LoadingState.Loading)

    private val _isCreatingBackup = MutableStateFlow(false)
    val isCreatingBackup = _isCreatingBackup.asStateFlow()

    private val _goToRestoreBackupScreen = SingleLiveEventWithoutData()
    val goToRestoreBackupScreen: LiveData<Unit> get() = _goToRestoreBackupScreen

    val backupResult: LiveData<PerformBackupUseCase.Result> get() = _backupResult
    private val _backupResult = SingleLiveEvent<PerformBackupUseCase.Result>()

    val configuration = configurationManager
        .configuration
        .stateIn(viewModelScope, SharingStarted.Eagerly, Configuration.Failure(BackupRepository.Result.Failure.NotConfigured))

    fun createBackup() = scope.launch {
        val configuration = configuration.first()
        if (configuration is Configuration.Failure) return@launch

        _isCreatingBackup.value = true
        val result = performBackupUseCase.performBackup()
        _backupResult.value = result
        onBackupAttempted(result)
        _isCreatingBackup.value = false
    }

    open fun onBackupAttempted(result: PerformBackupUseCase.Result) {}
    open fun onAttemptedToGoToRestoreBackupScreenWhenNotConfigured(configuration: Configuration.Failure) {}

    fun goToRestore() = viewModelScope.launch {
        when (val configuration = configuration.first()) {
            is Configuration.Failure -> onAttemptedToGoToRestoreBackupScreenWhenNotConfigured(configuration)
            is Configuration.Success -> _goToRestoreBackupScreen.call()
        }
    }
}
