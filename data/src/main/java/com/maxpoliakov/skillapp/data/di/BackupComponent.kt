package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.backup.BackupConfigurationManager
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import dagger.Binds
import dagger.BindsInstance
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Scope
import javax.inject.Singleton

/***
 * Since instances of BackupComponent are provided as @Singleton,
 * this scope is essentially the same as the @Singleton scope,
 * but for dependencies, provided by BackupComponent.
 * */
@Scope
annotation class BackupComponentScoped

@BackupComponentScoped
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

@MapKey
annotation class BackupBackendKey(val value: BackupBackend)

@Module(subcomponents = [BackupComponent::class])
@InstallIn(SingletonComponent::class)
interface BackupSubcomponentModule {
    @Binds
    @IntoMap
    @BackupBackendKey(BackupBackend.Local)
    fun bindLocalBackupSubcomponentIntoMap(@Local backupComponent: BackupComponent): BackupComponent

    companion object {
        @Provides
        @Local
        @Singleton
        fun provideLocalBackupSubcomponent(factory: BackupComponent.Factory): BackupComponent {
            return factory.create(BackupBackend.Local)
        }
    }
}
