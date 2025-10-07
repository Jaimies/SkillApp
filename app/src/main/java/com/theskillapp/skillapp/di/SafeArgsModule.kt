package com.theskillapp.skillapp.di

import androidx.lifecycle.SavedStateHandle
import com.theskillapp.skillapp.ui.restore.RestoreBackupFragmentArgs
import com.theskillapp.skillapp.ui.skilldetail.SkillDetailFragmentArgs
import com.theskillapp.skillapp.ui.skillgroup.SkillGroupFragmentArgs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class, FragmentComponent::class)
object SafeArgsModule {
    @Provides
    fun provideSkillDetailFragmentArgs(
        savedStateHandle: SavedStateHandle
    ) = SkillDetailFragmentArgs.fromSavedStateHandle(savedStateHandle)

    @Provides
    fun provideSkillGroupFragmentArgs(
        savedStateHandle: SavedStateHandle
    ) = SkillGroupFragmentArgs.fromSavedStateHandle(savedStateHandle)

    @Provides
    fun provideRestoreBackupFragmentArgs(
        savedStateHandle: SavedStateHandle
    ) = RestoreBackupFragmentArgs.fromSavedStateHandle(savedStateHandle)
}
