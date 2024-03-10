package com.maxpoliakov.skillapp.ui.restore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase.RestorationState
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestoreBackupViewModel @Inject constructor(
    restoreBackupUseCase: RestoreBackupUseCase,
    private val backupRepository: BackupRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _backups = MutableLiveData<List<Backup>>()
    val backups: LiveData<List<Backup>> get() = _backups

    val isRestorationInProgress = restoreBackupUseCase.state.map { state ->
        state == RestorationState.Active
    }.asLiveData()

    private val _showError = SingleLiveEventWithoutData()
    val showError: LiveData<Unit> get() = _showError

    init {
        getBackupsIfAuthenticated()
    }

    private fun getBackupsIfAuthenticated() = viewModelScope.launch {
        if (authRepository.currentUser.first() == null) return@launch

        try {
            val backups = backupRepository.getBackups()
            _backups.value = backups
        } catch (e: Exception) {
            _showError.call()
        }
    }
}
