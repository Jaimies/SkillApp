package com.maxpoliakov.skillapp.ui.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.User
import com.maxpoliakov.skillapp.domain.model.result.BackupResult
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.NetworkUtil
import com.maxpoliakov.skillapp.domain.usecase.backup.CreateBackupUseCase
import com.maxpoliakov.skillapp.shared.util.dateTimeFormatter
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.error.logToCrashlytics
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import com.maxpoliakov.skillapp.domain.model.result.BackupUploadResult.Failure as UploadFailure

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val createBackupUseCase: CreateBackupUseCase,
    private val backupRepository: BackupRepository,
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

    private val _showLogoutDialog = SingleLiveEvent<Nothing>()
    val showLogoutDialog: LiveData<Nothing> get() = _showLogoutDialog

    private val _showNoNetwork = SingleLiveEvent<Nothing>()
    val showNoNetwork: LiveData<Nothing> get() = _showNoNetwork

    private val _lastBackupDate = MutableLiveData<Any?>(R.string.loading_last_backup)
    val lastBackupDate: LiveData<Any?> get() = _lastBackupDate

    private val _showSnackbar = SingleLiveEvent<Int>()
    val showSnackbar: LiveData<Int> get() = _showSnackbar

    private val _requestAppDataPermission = SingleLiveEvent<Nothing>()
    val requestAppDataPermission: LiveData<Nothing> get() = _requestAppDataPermission

    init {
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
            val backup = backupRepository.getLastBackup()
            if (backup == null) _lastBackupDate.value = R.string.no_backup_found
            else _lastBackupDate.value = dateTimeFormatter.format(backup.creationDate)
        } catch (e: Exception) {
            e.logToCrashlytics()
            e.printStackTrace()
            _lastBackupDate.value = null
            _showSnackbar.value = R.string.something_went_wrong
        }
    }

    fun notifySignedIn() {
        _currentUser.value = authRepository.currentUser
        _lastBackupDate.value = R.string.loading_last_backup
        updateLastBackupDate()

        if (!authRepository.hasAppDataPermission)
            _requestAppDataPermission.call()
        else
            createBackupInBackground()

        logEvent("sign_in")
    }

    private fun createBackupInBackground() = ioScope.launch {
        val result = createBackupUseCase.createBackup()

        if (result is BackupResult.Success) {
            _lastBackupDate.postValue(dateTimeFormatter.format(LocalDateTime.now()))
        }
    }

    fun showLogoutDialog() = _showLogoutDialog.call()

    fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
        _lastBackupDate.value = R.string.loading_last_backup
        logEvent("sign_out")
    }

    fun signIn() {
        _signIn.call()
    }

    fun createBackup() = ioScope.launch {
        logEvent("create_backup")
        _backupCreating.postValue(true)
        val result = createBackupUseCase.createBackup()
        withContext(Dispatchers.Main) { handleResult(result) }
        _backupCreating.postValue(false)
    }

    private fun handleResult(result: BackupResult) {
        when (result) {
            is BackupResult.Success -> {
                _lastBackupDate.value = dateTimeFormatter.format(LocalDateTime.now())
                _showSnackbar.value = R.string.backup_successful
            }

            is BackupResult.CreationFailure -> {
                _showSnackbar.value = R.string.backup_creation_failed
            }

            is BackupResult.UploadFailure -> handleUploadFailure(result)
        }
    }

    private fun handleUploadFailure(result: BackupResult.UploadFailure) {
        when (val result = result.uploadResult) {
            is UploadFailure.NoInternetConnection -> {
                _showNoNetwork.call()
            }

            is UploadFailure.IOFailure -> {
                _showSnackbar.value = R.string.failed_to_reach_google_drive
            }

            is UploadFailure.QuotaExceeded -> {
                _showSnackbar.value = R.string.drive_out_of_space
            }

            is UploadFailure.PermissionDenied -> {
                _requestAppDataPermission.call()
            }

            is UploadFailure.Error -> {
                _showSnackbar.value = R.string.backup_upload_failed
                result.exception.logToCrashlytics()
            }

            else -> {}
        }
    }

    fun goToRestore() {
        if (!authRepository.hasAppDataPermission) _requestAppDataPermission.call()
        else if (!networkUtil.isConnected) _showNoNetwork.call()
        else _goToRestore.call()
    }
}
