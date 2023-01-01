package com.maxpoliakov.skillapp.di

import android.app.Activity
import android.view.View
import com.maxpoliakov.skillapp.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class SnackbarModule {
    @Provides
    @SnackbarRoot
    fun provideSnackbarRoot(activity: Activity): View {
        return activity.findViewById(R.id.snackbar_root)
    }
}
