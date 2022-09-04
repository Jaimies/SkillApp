package com.maxpoliakov.skillapp.di

import androidx.lifecycle.SavedStateHandle
import com.maxpoliakov.skillapp.ui.skilldetail.SkillDetailFragmentArgs
import com.maxpoliakov.skillapp.ui.skillgroup.SkillGroupFragmentArgs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object SafeArgsModule {
    @Provides
    fun provideSkillDetailFragmentArgs(
        savedStateHandle: SavedStateHandle
    ) = SkillDetailFragmentArgs.fromSavedStateHandle(savedStateHandle)

    @Provides
    fun provideSkillGroupFragmentArgs(
        savedStateHandle: SavedStateHandle
    ) = SkillGroupFragmentArgs.fromSavedStateHandle(savedStateHandle)
}
