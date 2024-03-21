package com.maxpoliakov.skillapp.ui.backup

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.shared.DataBindingFragment
import com.maxpoliakov.skillapp.shared.dialog.showSnackbar
import com.maxpoliakov.skillapp.shared.extensions.navigateAnimated

abstract class BackupFragment <BindingType: ViewDataBinding, ViewModelType: BackupViewModel>: DataBindingFragment<BindingType>() {
    abstract val viewModel: ViewModelType

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.backupResult.observe(viewLifecycleOwner, this::handleBackupResult)
        viewModel.goToRestoreBackupScreen.observe(viewLifecycleOwner) { navigateToRestoreBackupScreen() }
    }

    private fun navigateToRestoreBackupScreen() {
        findNavController().navigateAnimated(R.id.restore_backup_fragment_dest)
    }

    protected open fun handleBackupResult(result: PerformBackupUseCase.Result) {
        when (result) {
            is PerformBackupUseCase.Result.Success -> {
                showSnackbar(R.string.backup_successful)
            }

            is PerformBackupUseCase.Result.CreationFailure -> {
                showSnackbar(R.string.backup_creation_failed)
            }

            is PerformBackupUseCase.Result.UploadFailure -> handleUploadFailure(result)
        }
    }

    protected open fun handleUploadFailure(result: PerformBackupUseCase.Result.UploadFailure) {
        showSnackbar(R.string.failed_to_save_backup)
    }
}
