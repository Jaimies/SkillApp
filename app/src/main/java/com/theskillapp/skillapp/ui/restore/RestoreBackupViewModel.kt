package com.theskillapp.skillapp.ui.restore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.theskillapp.skillapp.data.di.BackupBackend
import com.theskillapp.skillapp.data.di.BackupComponent
import com.theskillapp.skillapp.domain.repository.BackupRepository.Result
import com.theskillapp.skillapp.domain.usecase.backup.RestoreBackupUseCase.RestorationState
import com.theskillapp.skillapp.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RestoreBackupViewModel @Inject constructor(
    backupComponentsByBackend: Map<BackupBackend, @JvmSuppressWildcards BackupComponent>,
    args: RestoreBackupFragmentArgs,
) : ViewModel() {
    private val component = backupComponentsByBackend[args.backupBackend]!!
    private val backupRepository = component.backupRepository()
    private val restoreBackupUseCase = component.restoreBackupUseCase()

    val backups = flow { emit(backupRepository.getBackups()) }
        .map { result ->
            if (result is Result.Success) LoadingState.Success(result.value)
            else LoadingState.Error
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, LoadingState.Loading)
        .asLiveData()

    val isRestorationInProgress = restoreBackupUseCase.state.map { state ->
        state == RestorationState.Active
    }.asLiveData()
}
