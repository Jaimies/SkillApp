package com.theskillapp.skillapp.ui.backup.shared_storage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.data.di.BackupBackend
import com.theskillapp.skillapp.data.extensions.toGenericUri
import com.theskillapp.skillapp.databinding.SharedStorageBackupFragBinding
import com.theskillapp.skillapp.shared.extensions.viewModelFromAssistedFactory
import com.theskillapp.skillapp.shared.fragment.observe
import com.theskillapp.skillapp.ui.backup.BackupFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SharedStorageBackupFragment: BackupFragment<SharedStorageBackupFragBinding, SharedStorageBackupViewModel>() {
    override val layoutId = R.layout.shared_storage_backup_frag
    override val backend = BackupBackend.Local

    override val viewModel by viewModelFromAssistedFactory<SharedStorageBackupViewModel.Factory, SharedStorageBackupViewModel> { factory ->
        factory.create(
            backupComponent.backupRepository(),
            backupComponent.configuration(),
            backupComponent.performBackupUseCase()
        )
    }

    private val directoryPickerLauncher = registerForActivityResult(
        StartActivityForResult(),
        this::onDirectoryPicked,
    )

    override fun onBindingCreated(binding: SharedStorageBackupFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.showExportDirectoryPicker) { showExportDirectoryPicker() }
    }

    private fun onDirectoryPicked(result: ActivityResult) {
        result
            .takeIf { it.resultCode == Activity.RESULT_OK }
            ?.data
            ?.data
            ?.toGenericUri()
            ?.let(viewModel::setExportDirectory)
    }

    private fun showExportDirectoryPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        directoryPickerLauncher.launch(intent)
    }
}
