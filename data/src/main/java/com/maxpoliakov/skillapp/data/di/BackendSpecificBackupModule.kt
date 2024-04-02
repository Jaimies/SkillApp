package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.backup.BackupConfigurationManager
import com.maxpoliakov.skillapp.data.file_system.FileSystem
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Provider

@Module
@DisableInstallInCheck
object BackendSpecificBackupModule {
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
