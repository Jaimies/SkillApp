package com.maxpoliakov.skillapp.di

import android.content.Context
import com.maxpoliakov.skillapp.data.backup.BackupConfigurationManager
import com.maxpoliakov.skillapp.data.di.AndroidSystemModule
import com.maxpoliakov.skillapp.data.di.BackendSpecificBackupModule
import com.maxpoliakov.skillapp.data.di.BackupBackend
import com.maxpoliakov.skillapp.data.di.BackupModule
import com.maxpoliakov.skillapp.data.di.DatabaseModule
import com.maxpoliakov.skillapp.data.di.DriveModule
import com.maxpoliakov.skillapp.data.di.GoogleSignInModule
import com.maxpoliakov.skillapp.data.di.JsonModule
import com.maxpoliakov.skillapp.data.di.PersistenceModule
import com.maxpoliakov.skillapp.data.di.RepositoryModule
import com.maxpoliakov.skillapp.di.coroutines.CoroutineScopesModule
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase
import dagger.BindsInstance
import dagger.Component
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        BackupModule::class,
        BackendSpecificBackupModule::class,
        RepositoryModule::class,
        PersistenceModule::class,
        GoogleSignInModule::class,
        UtilityModule::class,
        AndroidSystemModule::class,
        UseCaseModule::class,
        BackupModule::class,
        DriveModule::class,
        DatabaseModule::class,
        JsonModule::class,
        CoroutineScopesModule::class,
    ],
)
interface BackupComponent {
    fun configuration(): BackupConfigurationManager
    fun backupRepository(): BackupRepository
    fun performBackupUseCase(): PerformBackupUseCase
    fun restoreBackupUseCase(): RestoreBackupUseCase
    fun fragmentSubcomponentFactory(): FragmentBackupComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @ApplicationContext context: Context,
            @BindsInstance backend: BackupBackend
        ): BackupComponent
    }
}
