package com.maxpoliakov.skillapp.ui.backup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.BackupFragBinding
import com.maxpoliakov.skillapp.ui.common.DataBindingFragment
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.dialog.showDialog
import com.maxpoliakov.skillapp.util.dialog.showSnackbar
import com.maxpoliakov.skillapp.util.dialog.showToast
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BackupFragment : DataBindingFragment<BackupFragBinding>() {
    override val layoutId get() = R.layout.backup_frag

    private val viewModel: BackupViewModel by viewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    private val signInLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            GoogleSignIn.getSignedInAccountFromIntent(result.data)
                .addOnSuccessListener { googleAccount ->
                    val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
                    val account = googleAccount.account!!
                    prefs.edit {
                        putString("account.name", account.name)
                        putString("account.type", account.type)
                    }

                    viewModel.notifySignedIn()
                }
        }
    }

    override fun onBindingCreated(binding: BackupFragBinding) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.signIn) { signIn() }
        observe(viewModel.goToRestore) {
            findNavController().navigateAnimated(R.id.restore_backup_fragment_dest)
        }

        observe(viewModel.showNoNetwork) { showToast(R.string.no_internet) }
        observe(viewModel.showSnackbar, this::showSnackbar)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_APPDATA_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.updateLastBackupDate()
            logEvent("grant_appdata_permission")
        }
    }

    private fun signIn() {
        signInLauncher.launch(googleSignInClient.signInIntent)
    }

    companion object {
        private const val REQUEST_APPDATA_CODE = 0
    }
}
