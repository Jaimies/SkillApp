package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.data.backup.BackupConfigurationManager
import com.maxpoliakov.skillapp.data.di.BackupBackend
import com.maxpoliakov.skillapp.data.di.BackupModule
import com.maxpoliakov.skillapp.data.di.GoogleDrive
import com.maxpoliakov.skillapp.data.di.Local
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Subcomponent(modules = [BackupModule::class])
interface BackupComponent {
    fun configuration(): BackupConfigurationManager
    fun backupRepository(): BackupRepository
    fun performBackupUseCase(): PerformBackupUseCase
    fun restoreBackupUseCase(): RestoreBackupUseCase

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance backend: BackupBackend): BackupComponent
    }
}

@Module(subcomponents = [BackupComponent::class])
@InstallIn(SingletonComponent::class)
object BackupSubcomponentModule {
    @Provides
    @Local
    fun provideLocalBackupSubcomponent(factory: BackupComponent.Factory): BackupComponent {
        return factory.create(BackupBackend.Local)
    }

    @Provides
    @GoogleDrive
    fun provideGoogleDriveBackupSubcomponent(factory: BackupComponent.Factory): BackupComponent {
        return factory.create(BackupBackend.GoogleDrive)
    }
}
