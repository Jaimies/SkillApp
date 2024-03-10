package com.maxpoliakov.skillapp.ui.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.di.ApplicationScope
import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.NetworkUtil
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
    val currentUser = authRepository.currentUser.asLiveData()

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

    val lastBackupState = backupRepository
        .getLastBackupFlow()
        .map {
            if (it is BackupRepository.Result.Success) LoadingState.Success(it.value)
            LoadingState.Error
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, LoadingState.Loading)

    private val _showSnackbar = SingleLiveEvent<Int>()
    val showSnackbar: LiveData<Int> get() = _showSnackbar

    private val _requestAppDataPermission = SingleLiveEventWithoutData()
    val requestAppDataPermission: LiveData<Unit> get() = _requestAppDataPermission

    init {
        viewModelScope.launch { init() }
    }

    private suspend fun init() {
        if (!networkUtil.isConnected) _showNoNetwork.call()
        else if (userAuthenticatedButPermissionNotGranted()) _requestAppDataPermission.call()
    }

    fun notifySignedIn() = viewModelScope.launch {
        authRepository.reportSignIn()

        if (userAuthenticatedButPermissionNotGranted())
            _requestAppDataPermission.call()
        else
            createBackupInBackground()
    }

    private suspend fun userAuthenticatedButPermissionNotGranted() : Boolean {
        val user = authRepository.currentUser.first()
        return user != null && !user.hasAppDataPermission
    }

    private fun createBackupInBackground() = scope.launch {
        performBackupUseCase.performBackup()
    }

    fun showLogoutDialog() = _showLogoutDialog.call()

    fun signOut() {
        authRepository.signOut()
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
        val user = currentUser.value ?: return
        if (!user.hasAppDataPermission) _requestAppDataPermission.call()
        else if (!networkUtil.isConnected) _showNoNetwork.call()
        else _goToRestore.call()
    }

    sealed class LoadingState {
        object Loading : LoadingState()
        object Error : LoadingState()

        data class Success(val backup: Backup?) : LoadingState()
    }
}
