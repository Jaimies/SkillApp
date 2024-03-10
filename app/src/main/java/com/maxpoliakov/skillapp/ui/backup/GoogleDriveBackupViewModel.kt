package com.maxpoliakov.skillapp.ui.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.di.ApplicationScope
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.NetworkUtil
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoogleDriveBackupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val performBackupUseCase: PerformBackupUseCase,
    backupRepository: BackupRepository,
    private val networkUtil: NetworkUtil,
    @ApplicationScope
    private val scope: CoroutineScope,
) : BackupViewModel(performBackupUseCase, backupRepository, scope) {
    val currentUser = authRepository.currentUser.asLiveData()

    private val _signIn = SingleLiveEventWithoutData()
    val signIn: LiveData<Unit> get() = _signIn

    private val _showLogoutDialog = SingleLiveEventWithoutData()
    val showLogoutDialog: LiveData<Unit> get() = _showLogoutDialog

    private val _showNoNetwork = SingleLiveEventWithoutData()
    val showNoNetwork: LiveData<Unit> get() = _showNoNetwork

    private val _requestAppDataPermission = SingleLiveEventWithoutData()
    val requestAppDataPermission: LiveData<Unit> get() = _requestAppDataPermission

    override val isConfigured = authRepository.currentUser.map { user ->
        user != null && user.hasAppDataPermission
    }

    private val isAuthenticatedButPermissionNotGranted = authRepository.currentUser.map { user ->
        user != null && !user.hasAppDataPermission
    }

    init {
        viewModelScope.launch { init() }
    }

    private suspend fun init() {
        if (!networkUtil.isConnected) _showNoNetwork.call()
        else if (isAuthenticatedButPermissionNotGranted.first()) _requestAppDataPermission.call()
    }

    override fun onAttemptedToGoToRestoreBackupScreenWhenNotConfigured() {
        _requestAppDataPermission.call()
    }

    fun notifySignedIn() = viewModelScope.launch {
        authRepository.reportSignIn()

        if (isAuthenticatedButPermissionNotGranted.first()) _requestAppDataPermission.call()
        else createBackupInBackground()
    }

    private fun createBackupInBackground() = scope.launch {
        performBackupUseCase.performBackup()
    }

    fun showLogoutDialog() = _showLogoutDialog.call()
    fun signIn() = _signIn.call()
    fun signOut() = authRepository.signOut()

    override fun handleUploadFailure(result: PerformBackupUseCase.Result.UploadFailure) {
        when (result.uploadResult) {
            is BackupRepository.Result.Failure.NoInternetConnection -> {
                _showNoNetwork.call()
            }

            is BackupRepository.Result.Failure.IOFailure -> {
                showSnackbar(R.string.failed_to_reach_google_drive)
            }

            is BackupRepository.Result.Failure.QuotaExceeded -> {
                showSnackbar(R.string.drive_out_of_space)
            }

            is BackupRepository.Result.Failure.PermissionDenied -> {
                _requestAppDataPermission.call()
            }

            is BackupRepository.Result.Failure.Error -> {
                showSnackbar(R.string.backup_upload_failed)
            }

            else -> {}
        }
    }
}
