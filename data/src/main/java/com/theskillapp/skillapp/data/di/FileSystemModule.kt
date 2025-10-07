package com.theskillapp.skillapp.data.di

import com.theskillapp.skillapp.data.file_system.PermissionManager
import com.theskillapp.skillapp.data.file_system.SharedStoragePermissionManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FileSystemModule {
    @Binds
    fun bindPermissionManager(permissionManager: SharedStoragePermissionManager): PermissionManager
}
