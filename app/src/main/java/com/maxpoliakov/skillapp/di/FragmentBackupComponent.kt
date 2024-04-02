package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.data.di.BackupBackend
import com.maxpoliakov.skillapp.data.di.BackupModule
import com.maxpoliakov.skillapp.ui.restore.BackupListAdapter
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Subcomponent(modules = [BackupModule::class])
interface FragmentBackupComponent {
    fun backupListAdapter(): BackupListAdapter

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance backend: BackupBackend): FragmentBackupComponent
    }
}
@Module(subcomponents = [FragmentBackupComponent::class])
@InstallIn(FragmentComponent::class)
interface FragmentBackupSubcomponentModule
