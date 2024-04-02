package com.maxpoliakov.skillapp.ui.backup.google_drive

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.data.di.BackupBackend
import com.maxpoliakov.skillapp.databinding.GoogleDriveBackupFragBinding
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.shared.dialog.showDialog
import com.maxpoliakov.skillapp.shared.dialog.showSnackbar
import com.maxpoliakov.skillapp.shared.dialog.showToast
import com.maxpoliakov.skillapp.shared.extensions.viewModelFromAssistedFactory
import com.maxpoliakov.skillapp.shared.fragment.observe
import com.maxpoliakov.skillapp.ui.backup.BackupFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GoogleDriveBackupFragment : BackupFragment<GoogleDriveBackupFragBinding, GoogleDriveBackupViewModel>() {
    override val layoutId get() = R.layout.google_drive_backup_frag
    override val backend = BackupBackend.GoogleDrive

    override val viewModel by viewModelFromAssistedFactory<GoogleDriveBackupViewModel.Factory, GoogleDriveBackupViewModel> { factory ->
        factory.create(
            backupComponent.backupRepository(),
            backupComponent.configuration(),
            backupComponent.performBackupUseCase()
        )
    }

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    private val signInLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            GoogleSignIn
                .getSignedInAccountFromIntent(result.data)
                .addOnSuccessListener { viewModel.notifySignedIn() }
        }
    }

    override fun onBindingCreated(binding: GoogleDriveBackupFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.signIn) { signIn() }

        observe(viewModel.showNoNetwork) { showToast(R.string.no_internet) }

        observe(viewModel.showLogoutDialog) {
            requireContext().showDialog(R.string.confirm_logout, R.string.logout) {
                viewModel.signOut()
            }
        }
        observe(viewModel.requestAppDataPermission) {
            GoogleSignIn.requestPermissions(
                this,
                REQUEST_APPDATA_CODE,
                GoogleSignIn.getLastSignedInAccount(requireContext()),
                Scope(DriveScopes.DRIVE_APPDATA),
            )
        }
    }

    override fun handleUploadFailure(result: PerformBackupUseCase.Result.UploadFailure) {
        when (result.uploadResult) {
            is BackupRepository.Result.Failure.NoInternetConnection -> {
                showToast(R.string.no_internet)
            }

            is BackupRepository.Result.Failure.IOFailure -> {
                showSnackbar(R.string.failed_to_reach_google_drive)
            }

            is BackupRepository.Result.Failure.QuotaExceeded -> {
                showSnackbar(R.string.drive_out_of_space)
            }

            is BackupRepository.Result.Failure.Error -> {
                showSnackbar(R.string.backup_upload_failed)
            }

            else -> {}
        }
    }

    private fun signIn() {
        signInLauncher.launch(googleSignInClient.signInIntent)
    }

    companion object {
        private const val REQUEST_APPDATA_CODE = 0
    }
}
