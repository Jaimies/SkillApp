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
import com.maxpoliakov.skillapp.util.error.logToCrashlytics
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.util.network.NetworkUtil
import com.maxpoliakov.skillapp.util.time.dateTimeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val createBackupUseCase: CreateBackupUseCase,
    private val restoreBackupUseCase: RestoreBackupUseCase,
    private val driveRepository: DriveRepository,
    private val networkUtil: NetworkUtil,
    private val ioScope: CoroutineScope,
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

    private val _showError = SingleLiveEvent<Nothing>()
    val showError: LiveData<Nothing> get() = _showError

    private val _showBackupRestorationSucceeded = SingleLiveEvent<Nothing>()
    val showBackupRestorationSucceeded: LiveData<Nothing> = _showBackupRestorationSucceeded

    private val _showLogoutDialog = SingleLiveEvent<Nothing>()
    val showLogoutDialog: LiveData<Nothing> get() = _showLogoutDialog

    private val _showNoNetwork = SingleLiveEvent<Nothing>()
    val showNoNetwork: LiveData<Nothing> get() = _showNoNetwork

    private val _lastBackupDate = MutableLiveData<Any?>(R.string.loading_last_backup)
    val lastBackupDate: LiveData<Any?> get() = _lastBackupDate

    private val _requestAppDataPermission = SingleLiveEvent<Nothing>()
    val requestAppDataPermission: LiveData<Nothing> get() = _requestAppDataPermission

    init {
        viewModelScope.launch {
            restoreBackupUseCase.state.collectIgnoringInitialValue { state ->
                if (state == RestorationState.Finished) _showBackupRestorationSucceeded.call()
            }
        }

        updateLastBackupDate()

        if (!networkUtil.isConnected) _showNoNetwork.call()
        else if (authRepository.currentUser != null && !authRepository.hasAppDataPermission)
            _requestAppDataPermission.call()
    }

    fun updateLastBackupDate() = viewModelScope.launch {
        if (!networkUtil.isConnected || currentUser.value == null) {
            _lastBackupDate.value = null
            return@launch
        }

        if (!authRepository.hasAppDataPermission) {
            println("Not getting last backup date because the AppData permission is not granted")
            return@launch
        }

        try {
            val backup = driveRepository.getLastBackup()
            if (backup == null) _lastBackupDate.value = R.string.no_backup_found
            else _lastBackupDate.value = dateTimeFormatter.format(backup.creationDate)
        } catch (e: Exception) {
            e.logToCrashlytics()
            e.printStackTrace()
            _lastBackupDate.value = null
            _showError.call()
        }
    }

    fun notifySignedIn() {
        _currentUser.value = authRepository.currentUser
        _lastBackupDate.value = R.string.loading_last_backup
        updateLastBackupDate()

        if (!authRepository.hasAppDataPermission)
            _requestAppDataPermission.call()
    }

    fun signInOrSignOut() {
        if (currentUser.value == null) signIn()
        else _showLogoutDialog.call()
    }

    fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
        _lastBackupDate.value = R.string.loading_last_backup
    }

    private fun signIn() {
        _signIn.call()
    }

    fun createBackup() = ioScope.launch {
        if (!networkUtil.isConnected) {
            _showNoNetwork.postCall()
            return@launch
        }

        if (!authRepository.hasAppDataPermission) {
            _requestAppDataPermission.postCall()
            return@launch
        }

        _backupCreating.postValue(true)

        try {
            createBackupUseCase.createBackup()
            _lastBackupDate.postValue(dateTimeFormatter.format(LocalDateTime.now()))
            _showBackupCreationSucceeded.postCall()

        } catch (e: Exception) {
            e.logToCrashlytics()
            e.printStackTrace()
            _showError.postCall()
        }

        _backupCreating.postValue(false)
    }

    fun goToRestore() {
        if (!authRepository.hasAppDataPermission) _requestAppDataPermission.call()
        else if (!networkUtil.isConnected) _showNoNetwork.call()
        else _goToRestore.call()
    }
}
