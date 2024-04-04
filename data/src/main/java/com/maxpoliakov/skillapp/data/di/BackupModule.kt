package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.backup.BackupConfigurationManager
import com.maxpoliakov.skillapp.data.backup.BackupRepositoryImpl
import com.maxpoliakov.skillapp.data.backup.DBBackupCreator
import com.maxpoliakov.skillapp.data.backup.DBBackupRestorer
import com.maxpoliakov.skillapp.data.backup.google_drive.GoogleDriveBackupConfigurationManager
import com.maxpoliakov.skillapp.data.backup.shared_storage.SharedStorageBackupConfigurationManager
import com.maxpoliakov.skillapp.data.file_system.FileSystem
import com.maxpoliakov.skillapp.data.file_system.GoogleDriveFileSystem
import com.maxpoliakov.skillapp.data.file_system.SharedStorageFileSystem
import com.maxpoliakov.skillapp.domain.repository.BackupCreator
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRestorer
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCaseImpl
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Provider

@Module
@DisableInstallInCheck
interface BackupModule {
    @Binds
    fun bindBackupCreator(backupUtil: DBBackupCreator): BackupCreator

    @Binds
    fun bindBackupRestorer(backupUtil: DBBackupRestorer): BackupRestorer

    @Binds
    @GoogleDrive
    fun bindGoogleDriveBackupConfigurationManager(configurationManager: GoogleDriveBackupConfigurationManager): BackupConfigurationManager

    @Binds
    @GoogleDrive
    fun bindGoogleDriveFileSystem(fileSystem: GoogleDriveFileSystem): FileSystem

    @Binds
    @Local
    fun bindLocalBackupConfigurationManager(configurationManager: SharedStorageBackupConfigurationManager): BackupConfigurationManager

    @Binds
    @Local
    fun bindLocalFileSystem(fileSystem: SharedStorageFileSystem): FileSystem

    @Binds
    @BackupComponentScoped
    fun bindBackupRepository(repository: BackupRepositoryImpl): BackupRepository

    @Binds
    fun bindCreateBackupUseCase(useCase: PerformBackupUseCaseImpl): PerformBackupUseCase

    @Binds
    @BackupComponentScoped
    fun bindRestoreBackupUseCase(useCase: RestoreBackupUseCaseImpl): RestoreBackupUseCase

    companion object {
        @Provides
        fun provideBackupConfigurationManager(
            @Local localProvider: Provider<BackupConfigurationManager>,
            @GoogleDrive googleDriveProvider: Provider<BackupConfigurationManager>,
            backend: BackupBackend,
        ): BackupConfigurationManager {
            return when(backend) {
                BackupBackend.Local -> localProvider.get()
                BackupBackend.GoogleDrive -> googleDriveProvider.get()
            }
        }

        @Provides
        fun provideBackupFileSystem(
            @Local localProvider: Provider<FileSystem>,
            @GoogleDrive googleDriveProvider: Provider<FileSystem>,
            backend: BackupBackend,
        ): FileSystem {
            return when(backend) {
                BackupBackend.Local -> localProvider.get()
                BackupBackend.GoogleDrive -> googleDriveProvider.get()
            }
        }
    }
}
