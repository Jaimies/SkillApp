package com.maxpoliakov.skillapp.ui.backup

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.snackbar.Snackbar
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.BackupFragBinding
import com.maxpoliakov.skillapp.util.dialog.showDialog
import com.maxpoliakov.skillapp.util.dialog.showToast
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BackupFragment : Fragment() {
    private val viewModel: BackupViewModel by viewModels()

    private lateinit var binding: BackupFragBinding

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    private val signInLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.notifySignedIn()
            GoogleSignIn.getSignedInAccountFromIntent(result.data)
                .addOnSuccessListener { googleAccount ->
                    val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
                    val account = googleAccount.account!!
                    prefs.edit {
                        putString("account.name", account.name)
                        putString("account.type", account.type)
                    }
                }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BackupFragBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.signIn) { signIn() }
        observe(viewModel.goToRestore) {
            findNavController().navigateAnimated(R.id.restore_backup_fragment_dest)
        }

        observe(viewModel.showNoNetwork) { showToast(R.string.no_internet) }

        observe(viewModel.showBackupCreationSucceeded) {
            Snackbar.make(binding.root, R.string.backup_successful, Snackbar.LENGTH_SHORT).show()
        }
        observe(viewModel.showBackupRestorationSucceeded) {
            Snackbar.make(binding.root, R.string.backup_restore_successful, Snackbar.LENGTH_SHORT).show()
        }
        observe(viewModel.showLogoutDialog) {
            requireContext().showDialog(R.string.confirm_logout, R.string.logout) {
                viewModel.signOut()
            }
        }
    }

    private fun signIn() {
        signInLauncher.launch(googleSignInClient.signInIntent)
    }
}
