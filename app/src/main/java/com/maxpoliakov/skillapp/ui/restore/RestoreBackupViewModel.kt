package com.maxpoliakov.skillapp.ui.restore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository.Result
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase.RestorationState
import com.maxpoliakov.skillapp.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RestoreBackupViewModel @Inject constructor(
    restoreBackupUseCase: RestoreBackupUseCase,
    private val backupRepository: BackupRepository,
) : ViewModel() {

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
