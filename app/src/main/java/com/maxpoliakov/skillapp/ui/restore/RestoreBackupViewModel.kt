package com.maxpoliakov.skillapp.ui.restore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.DriveRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestoreBackupViewModel @Inject constructor(
    private val driveRepository: DriveRepository,
    restoreBackupUseCase: RestoreBackupUseCase,
    authRepository: AuthRepository,
) : ViewModel() {

    private val _backups = MutableLiveData<List<Backup>>()
    val backups: LiveData<List<Backup>> get() = _backups

    val isRestorationInProgress = restoreBackupUseCase.isRestorationInProgress.asLiveData()

    init {
        if (authRepository.currentUser != null)
            getBackups()
    }

    private fun getBackups() {
        viewModelScope.launch {
            val backups = driveRepository.getBackups()
            _backups.value = backups
        }
    }
}
