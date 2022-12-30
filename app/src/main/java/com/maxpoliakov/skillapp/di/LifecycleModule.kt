package com.maxpoliakov.skillapp.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
class LifecycleModule {
    @Provides
    fun provideLifecycleOwner(fragment: Fragment): LifecycleOwner {
        return fragment.viewLifecycleOwner
    }
}
