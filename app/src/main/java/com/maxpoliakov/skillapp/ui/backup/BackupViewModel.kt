package com.maxpoliakov.skillapp.ui.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.User
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.DriveRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.CreateBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.RestorationState
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import com.maxpoliakov.skillapp.shared.util.collectIgnoringInitialValue
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.time.dateTimeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val createBackupUseCase: CreateBackupUseCase,
    private val restoreBackupUseCase: RestoreBackupUseCase,
    private val driveRepository: DriveRepository,
) : ViewModel() {
    private val _currentUser = MutableLiveData(authRepository.currentUser)
    val currentUser: LiveData<User?> get() = _currentUser

    private val _signIn = SingleLiveEvent<Nothing>()
    val signIn: LiveData<Nothing> get() = _signIn

    private val _goToRestore = SingleLiveEvent<Nothing>()
    val goToRestore: LiveData<Nothing> get() = _goToRestore

    private val _backupCreating = MutableLiveData(false)
    val backupCreating: LiveData<Boolean> get() = _backupCreating

    private val _showBackupCreationSucceeded = SingleLiveEvent<Nothing>()
    val showBackupCreationSucceeded: LiveData<Nothing> = _showBackupCreationSucceeded

    private val _showBackupRestorationSucceeded = SingleLiveEvent<Nothing>()
    val showBackupRestorationSucceeded: LiveData<Nothing> = _showBackupRestorationSucceeded

    private val _lastBackupDate = MutableLiveData<Any>(R.string.loading_last_backup)
    val lastBackupDate: LiveData<Any> get() = _lastBackupDate

    init {
        viewModelScope.launch {
            restoreBackupUseCase.state.collectIgnoringInitialValue { state ->
                if (state == RestorationState.Finished) _showBackupRestorationSucceeded.call()
            }
        }

        getLastBackupDate()
    }

    private fun getLastBackupDate() = viewModelScope.launch {
        val backup = driveRepository.getLastBackup()
        if (backup == null) _lastBackupDate.value = R.string.no_backup_found
        else _lastBackupDate.value = dateTimeFormatter.format(backup.creationDate)
    }

    fun notifySignedIn() {
        _currentUser.value = authRepository.currentUser
        getLastBackupDate()
    }

    fun signInOrSignOut() {
        if (currentUser.value == null) signIn()
        else signOut()
    }

    private fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
        _lastBackupDate.value = R.string.loading_last_backup
    }

    private fun signIn() {
        _signIn.call()
    }

    fun createBackup() = viewModelScope.launch {
        _backupCreating.value = true
        createBackupUseCase.createBackup()
        _backupCreating.value = false
        _lastBackupDate.value = dateTimeFormatter.format(LocalDateTime.now())
        _showBackupCreationSucceeded.call()
    }

    fun goToRestore() = _goToRestore.call()
}
