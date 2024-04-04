package com.maxpoliakov.skillapp.ui.backup

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.MainDirections
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.data.di.BackupBackend
import com.maxpoliakov.skillapp.data.di.BackupComponent
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.shared.DataBindingFragment
import com.maxpoliakov.skillapp.shared.dialog.showSnackbar
import com.maxpoliakov.skillapp.shared.extensions.navigateAnimated
import javax.inject.Inject

abstract class BackupFragment <BindingType: ViewDataBinding, ViewModelType: BackupViewModel>: DataBindingFragment<BindingType>() {
    protected abstract val viewModel: ViewModelType
    protected abstract val backend: BackupBackend

    protected lateinit var backupComponent: BackupComponent

    @Inject
    lateinit var backupComponentsByBackend : Map<BackupBackend, @JvmSuppressWildcards BackupComponent>

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        backupComponent = backupComponentsByBackend[backend]!!
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.backupResult.observe(viewLifecycleOwner, this::handleBackupResult)
        viewModel.goToRestoreBackupScreen.observe(viewLifecycleOwner) { navigateToRestoreBackupScreen() }
    }

    private fun navigateToRestoreBackupScreen() {
        val directions = MainDirections.actionToRestoreBackupFragment(backend)
        findNavController().navigateAnimated(directions)
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
