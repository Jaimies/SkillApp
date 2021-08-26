package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.backup.BackupUtilImpl
import com.maxpoliakov.skillapp.domain.repository.BackupUtil
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BackupModule {
    @Binds
    fun provideBackupUtil(backupUtil: BackupUtilImpl): BackupUtil
}
