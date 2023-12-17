package com.maxpoliakov.skillapp.ui.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.di.ApplicationScope
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.User
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.NetworkUtil
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import com.maxpoliakov.skillapp.shared.util.dateTimeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val performBackupUseCase: PerformBackupUseCase,
    private val backupRepository: BackupRepository,
    private val networkUtil: NetworkUtil,
    @ApplicationScope
    private val scope: CoroutineScope,
) : ViewModel() {
    private val _currentUser = MutableLiveData(authRepository.currentUser)
    val currentUser: LiveData<User?> get() = _currentUser

    private val _signIn = SingleLiveEventWithoutData()
    val signIn: LiveData<Unit> get() = _signIn

    private val _goToRestore = SingleLiveEventWithoutData()
    val goToRestore: LiveData<Unit> get() = _goToRestore

    private val _backupCreating = MutableLiveData(false)
    val backupCreating: LiveData<Boolean> get() = _backupCreating

    private val _showLogoutDialog = SingleLiveEventWithoutData()
    val showLogoutDialog: LiveData<Unit> get() = _showLogoutDialog

    private val _showNoNetwork = SingleLiveEventWithoutData()
    val showNoNetwork: LiveData<Unit> get() = _showNoNetwork

    private val _lastBackupDate = MutableLiveData<Any?>(R.string.loading_last_backup)
    val lastBackupDate: LiveData<Any?> get() = _lastBackupDate

    private val _showSnackbar = SingleLiveEvent<Int>()
    val showSnackbar: LiveData<Int> get() = _showSnackbar

    private val _requestAppDataPermission = SingleLiveEventWithoutData()
    val requestAppDataPermission: LiveData<Unit> get() = _requestAppDataPermission

    init {
        updateLastBackupDate()

        if (!networkUtil.isConnected) _showNoNetwork.call()
        else if (authRepository.currentUser != null && !authRepository.hasAppDataPermission)
            _requestAppDataPermission.call()
    }

    fun updateLastBackupDate() = viewModelScope.launch {
        val result = backupRepository.getLastBackup()
        _lastBackupDate.value = getLastBackupDateValue(result)
    }

    private fun getLastBackupDateValue(result: BackupRepository.Result<Backup?>): Any? {
        if (result is BackupRepository.Result.Success) {
            return getLastBackupDateValue(result)
        } else {
            return R.string.failed_to_load_last_backup_date
        }
    }

    private fun getLastBackupDateValue(result: BackupRepository.Result.Success<Backup?>): Any {
        return result.value?.creationDate?.let(dateTimeFormatter::format)
            ?: R.string.no_backup_found
    }

    fun notifySignedIn() {
        _currentUser.value = authRepository.currentUser
        _lastBackupDate.value = R.string.loading_last_backup
        updateLastBackupDate()

        if (!authRepository.hasAppDataPermission)
            _requestAppDataPermission.call()
        else
            createBackupInBackground()
    }

    private fun createBackupInBackground() = scope.launch {
        val result = performBackupUseCase.performBackup()

        if (result is PerformBackupUseCase.Result.Success) {
            _lastBackupDate.postValue(dateTimeFormatter.format(LocalDateTime.now()))
        }
    }

    fun showLogoutDialog() = _showLogoutDialog.call()

    fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
        _lastBackupDate.value = R.string.loading_last_backup
    }

    fun signIn() {
        _signIn.call()
    }

    fun createBackup() = scope.launch {
        _backupCreating.value = true
        val result = performBackupUseCase.performBackup()
        handleResult(result)
        _backupCreating.value = false
    }

    private fun handleResult(result: PerformBackupUseCase.Result) {
        when (result) {
            is PerformBackupUseCase.Result.Success -> {
                _lastBackupDate.value = dateTimeFormatter.format(LocalDateTime.now())
                _showSnackbar.value = R.string.backup_successful
            }

            is PerformBackupUseCase.Result.CreationFailure -> {
                _showSnackbar.value = R.string.backup_creation_failed
            }

            is PerformBackupUseCase.Result.UploadFailure -> handleUploadFailure(result)
        }
    }

    private fun handleUploadFailure(result: PerformBackupUseCase.Result.UploadFailure) {
        when (result.uploadResult) {
            is BackupRepository.Result.Failure.NoInternetConnection -> {
                _showNoNetwork.call()
            }

            is BackupRepository.Result.Failure.IOFailure -> {
                _showSnackbar.value = R.string.failed_to_reach_google_drive
            }

            is BackupRepository.Result.Failure.QuotaExceeded -> {
                _showSnackbar.value = R.string.drive_out_of_space
            }

            is BackupRepository.Result.Failure.PermissionDenied -> {
                _requestAppDataPermission.call()
            }

            is BackupRepository.Result.Failure.Error -> {
                _showSnackbar.value = R.string.backup_upload_failed
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
