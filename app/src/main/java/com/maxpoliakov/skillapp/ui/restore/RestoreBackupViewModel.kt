package com.maxpoliakov.skillapp.ui.restore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.maxpoliakov.skillapp.di.DaggerBackupComponent
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository.Result
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase.RestorationState
import com.maxpoliakov.skillapp.model.LoadingState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RestoreBackupViewModel(
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

    companion object {
        val Factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[APPLICATION_KEY]!!
                val savedStateHandle = extras.createSavedStateHandle()
                val args = RestoreBackupFragmentArgs.fromSavedStateHandle(savedStateHandle)
                val component = DaggerBackupComponent.factory().create(application.applicationContext, args.backupBackend)

                @Suppress("UNCHECKED_CAST")
                return RestoreBackupViewModel(component.restoreBackupUseCase(), component.backupRepository()) as T
            }
        }
    }
}
