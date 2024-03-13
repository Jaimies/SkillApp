package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.file_system.FileSystemManager
import com.maxpoliakov.skillapp.data.file_system.GoogleDriveFileSystemManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FileSystemModule {
    @Binds
    fun provideGoogleDriveFileSystemManager(repository: GoogleDriveFileSystemManager): FileSystemManager
}
