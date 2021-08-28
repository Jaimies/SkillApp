package com.maxpoliakov.skillapp.ui.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.User
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.DriveRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.CreateBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val driveRepository: DriveRepository,
    private val createBackupUseCase: CreateBackupUseCase,
    private val restoreBackupUseCase: RestoreBackupUseCase,
) : ViewModel() {
    private val _currentUser = MutableLiveData(authRepository.currentUser)
    val currentUser: LiveData<User?> get() = _currentUser

    private val _signIn = SingleLiveEvent<Nothing>()
    val signIn: LiveData<Nothing> get() = _signIn

    private val _backups = MutableLiveData<List<Backup>>()
    val backups: LiveData<List<Backup>> get() = _backups

    init {
        if (currentUser.value != null)
            getBackups()
    }

    private fun getBackups() {
        viewModelScope.launch {
            val backups = driveRepository.getBackups()
            _backups.value = backups
        }
    }

    fun notifySignedIn() {
        _currentUser.value = authRepository.currentUser
    }

    fun restoreBackup() = viewModelScope.launch {
        restoreBackupUseCase.restoreBackup(backups.value!![0])
    }

    fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
    }

    fun signIn() {
        _signIn.call()
    }

    fun createBackup() = viewModelScope.launch {
        createBackupUseCase.createBackup()
    }
}
