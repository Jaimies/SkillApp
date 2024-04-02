package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.backup.BackupConfigurationManager
import com.maxpoliakov.skillapp.data.backup.DBBackupCreator
import com.maxpoliakov.skillapp.data.backup.DBBackupRestorer
import com.maxpoliakov.skillapp.data.backup.google_drive.GoogleDriveBackupConfigurationManager
import com.maxpoliakov.skillapp.data.backup.shared_storage.SharedStorageBackupConfigurationManager
import com.maxpoliakov.skillapp.data.file_system.FileSystem
import com.maxpoliakov.skillapp.data.file_system.GoogleDriveFileSystem
import com.maxpoliakov.skillapp.data.file_system.PermissionManager
import com.maxpoliakov.skillapp.data.file_system.SharedStorageFileSystem
import com.maxpoliakov.skillapp.data.file_system.SharedStoragePermissionManager
import com.maxpoliakov.skillapp.domain.repository.BackupCreator
import com.maxpoliakov.skillapp.domain.repository.BackupRestorer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
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
    fun bindPermissionManager(permissionManager: SharedStoragePermissionManager): PermissionManager
}
