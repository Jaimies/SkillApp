package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.backup.BackupCreatorImpl
import com.maxpoliakov.skillapp.data.backup.BackupRestorerImpl
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
    fun provideBackupCreator(backupUtil: BackupCreatorImpl): BackupCreator

    @Binds
    fun provideBackupRestorer(backupUtil: BackupRestorerImpl): BackupRestorer
}
