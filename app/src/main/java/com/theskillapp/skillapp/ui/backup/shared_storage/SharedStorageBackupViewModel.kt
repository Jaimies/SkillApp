package com.theskillapp.skillapp.ui.backup.shared_storage

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.theskillapp.skillapp.data.backup.BackupConfigurationManager
import com.theskillapp.skillapp.data.file_system.PermissionManager
import com.theskillapp.skillapp.domain.model.GenericUri
import com.theskillapp.skillapp.domain.repository.BackupRepository
import com.theskillapp.skillapp.domain.repository.UserPreferenceRepository
import com.theskillapp.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.theskillapp.skillapp.shared.lifecycle.SingleLiveEventWithoutData
import com.theskillapp.skillapp.ui.backup.BackupViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = SharedStorageBackupViewModel.Factory::class)
class SharedStorageBackupViewModel @AssistedInject constructor(
    @Assisted backupRepository: BackupRepository,
    @Assisted configurationManager: BackupConfigurationManager,
    @Assisted performBackupUseCase: PerformBackupUseCase,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val filePermissionManager: PermissionManager,
): BackupViewModel(backupRepository, configurationManager, performBackupUseCase) {

    val showExportDirectoryPicker: LiveData<Unit> get() = _showExportDirectoryPicker
    private val _showExportDirectoryPicker = SingleLiveEventWithoutData()

    val exportDirectory = userPreferenceRepository
        .backupExportDirectory
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun setExportDirectory(uri: GenericUri?) {
        userPreferenceRepository.setBackupExportDirectory(uri)

        if (uri != null) {
            filePermissionManager.persistReadAndWritePermissions(uri)
        } else {
            filePermissionManager.releaseAllReadAndWritePermissions()
        }
    }

    fun resetExportDirectory() = setExportDirectory(null)

    fun showExportDirectoryPicker() = _showExportDirectoryPicker.call()

    @AssistedFactory
    interface Factory {
        fun create(
            backupRepository: BackupRepository,
            configurationManager: BackupConfigurationManager,
            performBackupUseCase: PerformBackupUseCase,
        ): SharedStorageBackupViewModel
    }
}
