package com.maxpoliakov.skillapp.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.maxpoliakov.skillapp.util.tracking.RecordUtil
import com.maxpoliakov.skillapp.util.tracking.RecordUtilImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
interface FragmentModule {
    @Binds
    fun provideRecordUtil(recordUtil: RecordUtilImpl): RecordUtil

    companion object {
        @Provides
        @ChildFragmentManager
        fun provideChildFragmentManager(fragment: Fragment): FragmentManager {
            return fragment.childFragmentManager
        }

        @Provides
        fun provideContext(fragment: Fragment): Context {
            return fragment.requireContext()
        }
    }
}