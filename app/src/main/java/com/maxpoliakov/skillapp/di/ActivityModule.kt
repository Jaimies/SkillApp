package com.maxpoliakov.skillapp.di

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.maxpoliakov.skillapp.shared.snackbar.SnackbarShower
import com.maxpoliakov.skillapp.shared.snackbar.SnackbarShowerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface ActivityModule {
    @Binds
    fun provideSnackbarShower(snackbarShower: SnackbarShowerImpl): SnackbarShower

    companion object {
        @Provides
        fun provideFragmentManager(activity: Activity): FragmentManager {
            return (activity as AppCompatActivity).supportFragmentManager
        }
    }
}
