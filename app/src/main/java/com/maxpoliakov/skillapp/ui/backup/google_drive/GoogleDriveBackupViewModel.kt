package com.maxpoliakov.skillapp.ui.backup.google_drive

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.NetworkUtil
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import com.maxpoliakov.skillapp.ui.backup.BackupViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoogleDriveBackupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    backupRepository: BackupRepository,
    private val networkUtil: NetworkUtil,
) : BackupViewModel(backupRepository) {
    val currentUser = authRepository.currentUser.asLiveData()

    private val _signIn = SingleLiveEventWithoutData()
    val signIn: LiveData<Unit> get() = _signIn

    private val _showLogoutDialog = SingleLiveEventWithoutData()
    val showLogoutDialog: LiveData<Unit> get() = _showLogoutDialog

    private val _showNoNetwork = SingleLiveEventWithoutData()
    val showNoNetwork: LiveData<Unit> get() = _showNoNetwork

    private val _requestAppDataPermission = SingleLiveEventWithoutData()
    val requestAppDataPermission: LiveData<Unit> get() = _requestAppDataPermission

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

    override fun onBackupAttempted(result: PerformBackupUseCase.Result) {
        if (result is PerformBackupUseCase.Result.UploadFailure
            && result.uploadResult is BackupRepository.Result.Failure.PermissionDenied
        ) {
            _requestAppDataPermission.call()
        }
    }
}
