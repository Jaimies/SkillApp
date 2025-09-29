package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.data.di.BackupBackend
import com.maxpoliakov.skillapp.data.di.BackupBackendKey
import com.maxpoliakov.skillapp.data.di.Local
import com.maxpoliakov.skillapp.data.di.BackupComponent
import com.maxpoliakov.skillapp.data.di.BackupSubcomponentModule
import com.maxpoliakov.skillapp.domain.usecase.backup.StubPerformBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.StubRestoreBackupUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dagger.multibindings.IntoMap

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [BackupSubcomponentModule::class])
interface TestBackupSubcomponentModule {
    @Binds
    @IntoMap
    @BackupBackendKey(BackupBackend.Local)
    fun bindLocalBackupSubcomponentIntoMap(@Local backupComponent: BackupComponent): BackupComponent

    companion object {
        @Provides
        fun provideBackupSubcomponentFactory(): BackupComponent.Factory = TestBackupComponent.Factory

        @Provides
        @Local
        fun provideLocalBackupComponent(): BackupComponent = TestBackupComponent()
    }
}

class TestBackupComponent: BackupComponent {
    override fun configuration() = StubConfigurationManager()
    override fun backupRepository() = StubBackupRepository()
    override fun performBackupUseCase() = StubPerformBackupUseCase()
    override fun restoreBackupUseCase() = StubRestoreBackupUseCase()

    object Factory : BackupComponent.Factory {
        override fun create(backend: BackupBackend): BackupComponent {
            return TestBackupComponent()
        }
    }
}
